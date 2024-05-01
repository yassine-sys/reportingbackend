package com.example.backend.Controllers;

import com.example.backend.dao.UserRepository;
import com.example.backend.entities.Function;
import com.example.backend.entities.RepRapport;
import com.example.backend.entities.User;
import com.example.backend.entities.UserUpdateRequest;
import com.example.backend.services.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(method= RequestMethod.GET)
    public List<User> getList() {
        return userService.getListUser();
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value="/add/{id}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addUtilisateur(@RequestBody User user, @PathVariable Long id) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.addUser(user, id);
            return ResponseEntity.ok().body(new HashMap<String, String>() {{
                put("message", "User added successfully");
            }});
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value="/edit/{idGrp}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editUtilisateur(@RequestBody User user, @PathVariable Long idGrp) {
        System.out.println("===============Controller");

        user.setUser_group(null);

        // Check if password is not null and not empty
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            System.out.println("===============Controller0");
        } else {
            // If password is null or empty, retain the old password or handle accordingly
            // For now, assuming you want to keep the old password
            User existingUser = userService.findById(user.getuId());
            user.setPassword(existingUser.getPassword());
            System.out.println("===============Controller1");
        }

        System.out.println("===============Controller Finale");
        // user.setAdmin(user.isAdmin());
        userService.editUser(user, idGrp);
    }


    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @RequestMapping(value="/user/{id}",method=RequestMethod.GET)
    public User findbyId(@PathVariable Long id) {
        System.out.println("find by id d5al lel fonction c bon ");
        return userService.findById(id);
    }
    @RequestMapping(value="/email/{email}",method=RequestMethod.GET)
    public User findbyEmail(@PathVariable String email) {
        Optional<User> user =userService.findByEmail(email);
        return user.get();
    }

    @RequestMapping(value="/username/{username}",method=RequestMethod.GET)
    public User findbyUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.get();
    }

    @RequestMapping(value="/email2/{email}",method=RequestMethod.GET)
    public boolean findbyEmail2(@PathVariable String email) {
        Optional<User> user =userService.findByEmail(email);
        if(user.get()!=null){
            return true;
        }else {
            return false;
        }
    }

    @RequestMapping(value="/username2/{username}",method=RequestMethod.GET)
    public boolean findbyUsername2(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if(user.get()!=null){
            return true;
        }else {
            return false;
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @RequestMapping(value="/assign/{id}", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public void assignFunction(@PathVariable Long id, @RequestBody List<RepRapport> rap) {
        userService.assignFunc(id, rap);
    }
    @PreAuthorize("hasRole('Admin')")

    @RequestMapping(value="/detach/{id}", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public void detachRep(@PathVariable Long id, @RequestBody RepRapport rep) {
        userService.detachRep(id,rep);
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value="/assignbyid/{id}/{idrep}")
    public void assignRapport(@PathVariable Long id, @PathVariable Long idrep) {
        userService.assignRapport(id,idrep);
    }
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value="/removerapport/{id}/{idrep}")
    public void removeRapport(@PathVariable Long id, @PathVariable Long idrep) {
        userService.removeRapport(id,idrep);
    }

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/{userId}/reports")
    public List<RepRapport> getRepRapportsByFunctionIdOrdered(@PathVariable Long userId) {
        System.out.println(userService.findReportsByUserId(userId));
        List<BigInteger>RepRaportIds=userService.findReportsByUserId(userId);
        List<RepRapport> repRapports = new ArrayList<>();
        for (BigInteger reprapportId:RepRaportIds){
            RepRapport rapport=entityManager.find(RepRapport.class,reprapportId.longValue());
            if (rapport!=null){
                repRapports.add(rapport);
            }
        }
        return repRapports;
    }

    @GetMapping("/reports/{userId}")
    public List<BigInteger> getRepRapportsById(@PathVariable Long userId) {
        System.out.println(userService.findReportsByUserId(userId));
        List<BigInteger>RepRaportIds=userService.findReportsByUserId(userId);
        return RepRaportIds;
    }


    @PutMapping("/{userId}/reports/{repId}/order")
    public ResponseEntity<?> updateReportOrderForFunction(@PathVariable Long userId, @PathVariable Long repId , @RequestParam Long newOrder){
        Optional<User> optionalFunction =userRepository.findById(userId);
        if (optionalFunction.isPresent()){
            User  user=optionalFunction.get();
            List<RepRapport> rapports = user.getListreprapport();
            for (RepRapport rapport:rapports) {
                if (rapport.getId().equals(repId)){
                    userRepository.updateReportOrderForUser(newOrder,userId,repId);
                    return ResponseEntity.ok().build();
                }
            }
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{username}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable("username") String username,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword) {
        try {
            User updatedUser = userService.updatePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
}
}

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        // Retrieve the user from the database
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the old password matches the one in the database
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password");
        }

        // Update the fields that can be changed
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        if (request.getNomUtilisateur() != null && !request.getNomUtilisateur().isEmpty()) {
            user.setNomUtilisateur(request.getNomUtilisateur());
        }
        if (request.getUMail() != null && !request.getUMail().isEmpty()) {
            user.setuMail(request.getUMail());
        }

        // Check if the user wants to change the password
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encodedPassword);
        }

        // Save the updated user information
        userRepository.save(user);

        return ResponseEntity.ok().body("{\"message\": \"User updated successfully\"}");
    }




}
//    @PutMapping("/{id}/password/{previousPassword}/{newPassword}")
//    public ResponseEntity<String> changePassword(@PathVariable Long id,
//                                                 @RequestParam("previousPassword") String previousPassword,
//                                                 @RequestParam("newPassword") String newPassword) {
//        Optional<User> user = userRepository.findById(id);
//
//        // Verify the previous password
//        if (!user.get().getPassword().equals(previousPassword)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect previous password");
//        }
//
//        // Update the password
//        user.get().setPassword(newPassword);
//        userRepository.save(user.get());
//        return ResponseEntity.ok("Password changed successfully");
//    }
//
//    @PutMapping("/{id}")
//    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
//        Optional<User> user = userRepository.findById(id);
//        user.get().setUsername(updatedUser.getUsername());
//        user.get().setuMail(updatedUser.getuMail());
//        user.get().setNomUtilisateur(updatedUser.getuMail());
//        user.get().setDateModif(new Date());
//        // Update other fields as needed
//        return userRepository.save(user.get());
//    }



