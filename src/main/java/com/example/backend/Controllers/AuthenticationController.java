package com.example.backend.Controllers;

import com.example.backend.dao.LogRepository;
import com.example.backend.entities.*;
import com.example.backend.entities.DTOResp.*;
import com.example.backend.entities.Module;
import com.example.backend.entities.conf.JwtTokenUtil;
import com.example.backend.services.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
public class AuthenticationController {
    private final LogRepository logsRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private SubModuleService subModuleService;

    @Autowired
    private PasswordResetServiceImpl passwordResetService;

    @Autowired
    private CaptchaService captchaService;
    private String tokenResp;
    private Optional<User> userResp;
    @Autowired
    public AuthenticationController(LogRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    @RequestMapping(value = "token/generate-token", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final Optional<User> user = userService.findByUsername(loginUser.getUsername());

            final String token = jwtTokenUtil.generateToken(user.get());
            System.out.println("token:"+token);
            // Return token in response header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",token);
            if(user.get()!=null){
                loginUser.setuId(user.get().getuId());
                loginUser.setuMail(user.get().getuMail());
                customLoginResp response = new customLoginResp();
                if(user.get().getUser_group()!= null){
                    response.setModules(moduleService.findModuleByGroup(user.get().getUser_group().getgId()));
                }else{
                    response.setModules(null);
                }
                response.setUser(loginUser);

                Logs log = new Logs();
                log.setUserId(user.get().getUsername());
                log.setAction("AUTH");
                log.setTableAffected("managment.user");
                log.setDate(LocalDateTime.now());
                log.setRowId(user.get().getuId());
                logsRepository.save(log);

                return ResponseEntity.ok().headers(headers).body(response);
            }else{
                return ResponseEntity.ok().headers(headers).build();
            }
    }

    @RequestMapping(value="/check", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<TokenValidity> test(@RequestParam(name = "token") String token) {
        TokenValidity validity = new TokenValidity();
        validity.setValid(false);
        if (token != null) {
            try {
                final User user = jwtTokenUtil.extractUser(token);
                final String username = jwtTokenUtil.getUsernameFromToken(token);
                final Optional<User> u = userService.findByUsername(username);
                if (username.equals(u.get().getUsername())) {
                    if (!jwtTokenUtil.isTokenExpired(token)) {
                        // Token is valid and not expired
                        validity.setValid(true);
                        validity.setRole(u.get().getRole());
                        return ResponseEntity.ok(validity);
                    } else {
                        // Token is expired
                        return ResponseEntity.ok(validity);
                    }
                } else {
                    // Username in token does not match user retrieved from database
                    return ResponseEntity.ok(validity);
                }
            } catch (ExpiredJwtException ex) {
                // Token has expired
                return ResponseEntity.ok(validity);
            } catch (Exception ex) {
                // Other exceptions occurred
                return ResponseEntity.ok(validity);
            }
        } else {
            // Token is null
            return ResponseEntity.ok(validity);
        }
    }

    @RequestMapping(value = "/loginResp", method = RequestMethod.GET)
    @Transactional(readOnly = true)  // Set readOnly to true for fetching data only
    public User getUserData(@RequestParam(name = "token") String token) {
        final Optional<User> user = userService.findByUsername(jwtTokenUtil.extractUser(token).getUsername());
        System.out.println(user.get().getUsername());

        if (token != null && user.isPresent()) {
            // Clone and sort the user's modules and sub-modules recursively
            List<Module> sortedModules = cloneAndSortModules(user.get().getUser_group().getModule_groups());

            // Create a new User object with the sorted module groups
            User sortedUser = new User();
            sortedUser.setuId(user.get().getuId());
            sortedUser.setUsername(user.get().getUsername());
            sortedUser.setuMail(user.get().getuMail());
            sortedUser.setNomUtilisateur(user.get().getNomUtilisateur());
            sortedUser.setRole(user.get().getRole());
            //sortedUser.setUser_group(user.get().getUser_group());
            //sortedUser.getUser_group().setModule_groups(sortedModules);

            return sortedUser;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/resp", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public List<ModuleDTO> getData(@RequestParam(name = "token") String token) {
        final Optional<User> user = userService.findByUsername(jwtTokenUtil.extractUser(token).getUsername());
        System.out.println(user.get().getUsername());
        List<ModuleDTO> result = new ArrayList<>();
        if (token != null && user.isPresent()) {
            Group grp = user.get().getUser_group();

            // Group functions by module and then by submodule
            Map<Module, Map<SubModule, List<Function>>> functionsByModule = grp.getListe_function()
                    .stream()
                    .collect(Collectors.groupingBy(
                            function -> function.getSubModule().getModule(),
                            Collectors.groupingBy(Function::getSubModule)
                    ));

            for (Map.Entry<Module, Map<SubModule, List<Function>>> moduleEntry : functionsByModule.entrySet()) {
                Module module = moduleEntry.getKey();

                ModuleDTO moduleDTO = new ModuleDTO();
                moduleDTO.setId(module.getId());
                moduleDTO.setModuleName(module.getModuleName());
                moduleDTO.setOrder(module.getOrder());

                List<SubModuleDTO> subModuleDTOs = new ArrayList<>();
                for (Map.Entry<SubModule, List<Function>> subModuleEntry : moduleEntry.getValue().entrySet()) {
                    SubModule subModule = subModuleEntry.getKey();

                    SubModuleDTO subModuleDTO = new SubModuleDTO();
                    subModuleDTO.setId(subModule.getId());
                    subModuleDTO.setSubModuleName(subModule.getSubModuleName());
                    subModuleDTO.setOrder(subModule.getOrder());

                    // List of FunctionDTO for each submodule
                    List<FunctionDTo> functionDTOs = subModuleEntry.getValue().stream()
                            .sorted(Comparator.comparing(Function::getOrder)) // Sort functions by order
                            .map(function -> {
                                FunctionDTo functionDTO = new FunctionDTo();
                                functionDTO.setId(function.getId());
                                functionDTO.setFunctionName(function.getFunctionName());
                                functionDTO.setOrder(function.getOrder()); // Set order in DTO

                                // Map listeReports
                                List<ReportDTO> reportDTOs = function.getListreprapport().stream().map(report -> {
                                    ReportDTO reportDTO = new ReportDTO();
                                    // Assuming ReportDTO has similar structure, map the necessary fields
                                    reportDTO.setId(report.getId());
                                    reportDTO.setName(report.getName());
                                    reportDTO.setTitle(report.getTitle());
                                    // Add more mappings as needed
                                    return reportDTO;
                                }).collect(Collectors.toList());

                                functionDTO.setListeReports(reportDTOs); // Assign the mapped report DTOs

                                return functionDTO;
                            })
                            .collect(Collectors.toList());

                    subModuleDTO.setListe_functions(functionDTOs);
                    subModuleDTOs.add(subModuleDTO);
                }

                // Sort submodules inside each module
                subModuleDTOs.sort(Comparator.comparing(SubModuleDTO::getOrder));
                moduleDTO.setListSubModule(subModuleDTOs);

                result.add(moduleDTO);
            }
        }

        // Sort the modules
        result.sort(Comparator.comparing(ModuleDTO::getOrder));

        return result;
    }

    private List<Module> cloneAndSortModules(List<Module> modules) {
        List<Module> sortedModules = new ArrayList<>(modules);

        // Sort modules based on their "order" field
        sortedModules.sort(Comparator.comparingInt(Module::getOrder));

        // Recursively sort sub-modules for each module
        sortedModules.forEach(this::sortModule);

        return sortedModules;
    }

    private void sortModule(Module module) {
        // Sort sub-modules based on their "order" field
        module.getList_sub_modules().sort(Comparator.comparingInt(SubModule::getOrder));
    }




    public static final String endpoint = "http://ip-api.com/json";




    @RequestMapping(value="/list", method = RequestMethod.GET)
    public List<User> getList(){
        return userService.getListUser();

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        // Check if the request contains an email or username
        if (request.getEmail() != null) {
            // Password reset by email
            Optional<User> user = userService.findByEmail(request.getEmail());
            if (user.get() != null) {
                // Generate and send a password reset token to the user's email
                passwordResetService.generateAndSendPasswordResetToken(user.get());
                return ResponseEntity.ok("Password reset link sent to the email address.");
            }
        } else if (request.getUsername() != null) {
            // Password reset by username
            Optional<User> user = userService.findByUsername(request.getUsername());
            if (user.get() != null) {
                // Generate and send a password reset token to the user's email
                passwordResetService.generateAndSendPasswordResetToken(user.get());
                return ResponseEntity.ok("Password reset link sent to the user's email.");
            }
        }

        return ResponseEntity.badRequest().body("Invalid email or username.");
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request) {
        // Call the service to update the pass
        userService.updatePass(request);
        return ResponseEntity.ok("Password updated successfully");
    }

    private Map<String, String> captchaStore = new HashMap<>();
    @GetMapping(value = "/captcha")
    public ResponseEntity<?> getCaptcha() {
        String captchaText = generateRandomText(6);
        String captchaKey = Long.toString(System.nanoTime()); // Simple key generation strategy
        captchaStore.put(captchaKey, captchaText);

        BufferedImage captchaImage = captchaService.generateCaptchaImage(captchaText);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(captchaImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Prepare the response object
            Map<String, String> response = new HashMap<>();
            response.put("captchaKey", captchaKey);
            response.put("captchaImage", base64Image);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exception
            return ResponseEntity.badRequest().body("Failed to generate CAPTCHA");
        }
    }

    private String generateRandomText(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder captchaText = new StringBuilder();
        Random rnd = new Random();
        while (captchaText.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            captchaText.append(chars.charAt(index));
        }
        return captchaText.toString();
    }

    @PostMapping("/validate-captcha")
    public ResponseEntity<?> validateCaptcha(@RequestBody Map<String, String> captchaResponse) {
        String captchaKey = captchaResponse.get("key");
        String captchaValue = captchaResponse.get("value");

        if (captchaValue != null && captchaValue.equals(captchaStore.get(captchaKey))) {
            captchaStore.remove(captchaKey); // Remove captcha from store after successful validation
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Captcha validation failed");
        }
    }
}

