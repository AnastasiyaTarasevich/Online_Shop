package com.example.kursach.Services;

import com.example.kursach.Model.Role;

import com.example.kursach.Model.User;
import com.example.kursach.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService
{

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

   public boolean createUser (User user)
   {
       if(userRepo.findByLogin(user.getLogin())!=null) return false;
       user.setActive(true);
       user.getRoles().add(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
   }

    public void userSavenewRole(String username, Map<String, String> form, User user) {
        user.setLogin(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public User findById(int id) {
        return userRepo.findById(id).orElse(null);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);


    }

    public void updateResetPasswordToken(String token, String email)
    {
        User user=userRepo.findByEmail(email);
        if(user!=null)
        {
            user.setResetPasswordToken(token);
            userRepo.save(user);
        }
        else {
          System.out.println("Такого нет!");/// сделать обработку
        }

    }
    public User getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(User customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);

        customer.setResetPasswordToken(null);
        userRepo.save(customer);
    }






}
