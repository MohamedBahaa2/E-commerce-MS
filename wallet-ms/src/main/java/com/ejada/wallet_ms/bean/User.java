package com.ejada.wallet_ms.bean;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User(Long userId,String email, String password){
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
