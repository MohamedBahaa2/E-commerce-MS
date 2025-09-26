package com.ejada.wallet_ms.controller;

import com.ejada.wallet_ms.bean.User;
import com.ejada.wallet_ms.security.JwtUtil;
import com.ejada.wallet_ms.service.UserService;
import com.ejada.wallet_ms.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final WalletService walletService;

    private final JwtUtil jwtUtil;
    public UserController(UserService userService, WalletService walletService, JwtUtil jwtUtil){
        this.userService = userService;
        this.walletService = walletService;
        this.jwtUtil = jwtUtil;
    }

    //Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        User savedUser = userService.registerUser(user);

        //create a wallet for the new user
        walletService.createWallet(user.getUserId());

        return ResponseEntity.ok(savedUser);
    }

    //login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.login(email, password);
        if (user.isPresent()) {
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.badRequest().body("Invalid Credentials");
        }
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserByID(@PathVariable Long id){
        return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
