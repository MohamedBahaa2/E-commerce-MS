package com.ejada.wallet_ms.service;

import com.ejada.wallet_ms.bean.User;
import com.ejada.wallet_ms.repository.TransactionRepository;
import com.ejada.wallet_ms.repository.UserRepository;
import com.ejada.wallet_ms.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    //signup
    public User registerUser(User user){

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(encoder.encode(user.getPassword())); //hash password
        return userRepo.save(user);
    }


    public Optional<User> login(String email, String password) {
        return userRepo.findByEmail(email)
                .filter(u -> encoder.matches( password, u.getPassword()));
    }

    //find a user by id
    public Optional<User> getUserById(Long id){
        return userRepo.findById(id);
    }


}
