package com.example.backend.services;

import com.example.backend.dao.GroupRepository;
import com.example.backend.dao.RepRapportRepository;
import com.example.backend.dao.RoleRepository;
import com.example.backend.dao.UserRepository;
import com.example.backend.entities.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service(value = "userService")
public class UserServiceImp implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository grpRep;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RepRapportRepository repRapportRepository;

    private final PasswordEncoder passwordEncoder;

    private GroupService grpServ;

    @PersistenceContext
    private EntityManager em;

    public UserServiceImp(PasswordEncoder passwordEncoder,GroupService grpServ) {
        this.passwordEncoder = passwordEncoder;
        this.grpServ = grpServ;
    }

    @Override
    public Object customResponse(Long id){

        String sql = "SELECT u_id, nom_utilisateur, u_pwd, u_login, u_mail FROM management.user WHERE u_id = :id";
        Query query = em.createNativeQuery(sql);
        query.setParameter("id", id);
        List<Object[]> resultList = query.getResultList();
        Object[] row = resultList.get(0);

        Map<String, Object> result = new HashMap<>();
        result.put("u_id", row[0]);
        result.put("nom_utilisateur", row[1]);
        result.put("u_pwd", row[2]);
        result.put("u_login", row[3]);
        result.put("u_mail", row[4]);
        return result;

    }
    public User addUser(User user,Long idGrp) {
        System.out.println("===============>password 3: "+user.getPassword());
        // Check if the role exists
        if (user.getRole() == null || user.getRole().getId() == null) {
            throw new IllegalArgumentException("Role ID must not be null");
        }
        Optional<Role> role = roleRepository.findById(user.getRole().getId());
        if (!role.isPresent()) {
            throw new IllegalArgumentException("Role not found");
        }
        Group grp = grpServ.findById(idGrp);
        System.out.println("id grp==========>"+idGrp);
        System.out.println("id grp2==========>"+grp.getgId());
        user.setUser_group(grp);
        user.setRole(role.get());
        return userRepository.save(user);
    }



    @Override
    public User editUser(User user,Long idGrp) {
        User u = findById(user.getuId());

        System.out.println("===============1");
        if(u!=null){
            System.out.println("===============2");
            u.setUsername(user.getUsername());
            u.setPassword(user.getPassword());
            u.setDateCreation(user.getDateCreation());
            u.setDateModif(user.getDateModif());
            u.setEtat(user.getEtat());
            u.setuLogin(user.getuLogin());
            u.setuMail(user.getuMail());
            u.setuMatricule(user.getuMatricule());
            u.setuDepart(user.getuDepart());
            u.setIdCreateur(user.getIdCreateur());
            u.setNomUtilisateur(user.getNomUtilisateur());
            Group grp = grpServ.findById(idGrp);
            System.out.println("===============>"+grp.getgName());
            u.setUser_group(grp);
            u.setResetToken(user.getResetToken());
            System.out.println("===============3");
        }
        return userRepository.save(u);
    }

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }


    @Override
    public void deleteUser(Long id) {
        User user = findById(id);
        user.setListreprapport(new ArrayList<>());
        userRepository.delete(user);

    }

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        return findUser;
    }

    @Override
    public Optional<User> findByEmail(String email){
        Optional<User> findUser = userRepository.findUsersByUMail(email);
        return findUser;
    }

    @Override
    public Optional<User> findByToken(String token){
        Optional<User> findUser = userRepository.findUserByResetToken(token);
        return findUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.print(username);
        Optional<User> optionalUsers = userRepository.findByUsername(username);

        optionalUsers
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        //return optionalUsers
        //      .map(CustomUserDetails::new).get();
        return new org.springframework.security.core.userdetails.User(optionalUsers.get().getUsername(), optionalUsers.get().getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("GROUP_ADMIN"));
    }

    @Override
    public void assignFunc(Long id, List<RepRapport> rap) {
        User existingUser = findById(id);
        List<RepRapport> rapports = existingUser.getListreprapport();
        if (rap.isEmpty()) {
            System.err.println("Error: no rapports to add");
            return;
        }
        boolean found = false;
        for (RepRapport r : rap) {
            if (!rapports.contains(r)) {
                rapports.add(r);
                found = true;
            }
        }
        if (found) {
            existingUser.setListreprapport(rapports);
            userRepository.save(existingUser);
        }
    }

    @Override
    public void detachRep(Long id,RepRapport rep){
        User existingUser = findById(id);
        List<RepRapport> rapports = existingUser.getListreprapport();
        if (rep==null) {
            System.err.println("Error: no rapports to add");
            return;
        }
        if(rapports.contains(rep)){
            rapports.remove(rep);
            existingUser.setListreprapport(rapports);
            userRepository.save(existingUser);
        }
    }

    @Override
    public void assignRapport(Long id,Long repid){
        RepRapport existingRapport = repRapportRepository.findFirstById(repid);
        User existingUser = findById(id);
        List<RepRapport> rapports = existingUser.getListreprapport();
        if (existingRapport==null) {
            System.err.println("Error: no rapports to add");
            return;
        }
            if (!rapports.contains(existingRapport)) {
                rapports.add(existingRapport);
            }
            existingUser.setListreprapport(rapports);
            userRepository.save(existingUser);
    }

    @Override
    public void removeRapport(Long id,Long repid){
        RepRapport existingRapport = repRapportRepository.findFirstById(repid);
        detachRep(id,existingRapport);
    }

    @Override
    public List<BigInteger> findReportsByUserId(Long userId) {
        return userRepository.findReportsByUserId(userId);
    }

    @Override
    public User updatePassword(String username, String oldPassword, String newPassword) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found");
        }

        if (!passwordEncoder.matches(oldPassword, userOptional.get().getPassword())) {
            throw new Exception("Old password does not match");
        }



        // Update the password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            userOptional.get().setPassword(passwordEncoder.encode(newPassword));
        }

        // user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userOptional.get());
        return userOptional.get();
    }


    public void updatePass(PasswordUpdateRequest request) {
        // Retrieve the user from the database by their reset token
        Optional<User> user = userRepository.findUserByResetToken(request.getToken());

        if (user.isPresent()) {
            System.out.println(user.get().getuMail());
            System.out.println(passwordEncoder.encode(request.getNewPassword()));
            System.out.println(request.getNewPassword());
                // Encode and set the new password
                user.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
                user.get().setResetToken(null);
                // Save the user with the updated password
                this.editUser(user.get(),user.get().getUser_group().getgId());
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
