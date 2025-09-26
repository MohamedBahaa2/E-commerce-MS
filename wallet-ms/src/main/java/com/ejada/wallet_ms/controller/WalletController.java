package com.ejada.wallet_ms.controller;

import com.ejada.wallet_ms.DTO.PaymentRequest;
import com.ejada.wallet_ms.DTO.PaymentResponse;
import com.ejada.wallet_ms.bean.Transaction;
import com.ejada.wallet_ms.bean.User;
import com.ejada.wallet_ms.bean.Wallet;
import com.ejada.wallet_ms.service.UserService;
import com.ejada.wallet_ms.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private final WalletService walletService;
    @Autowired
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService){
        this.walletService = walletService;
        this.userService = userService;
    }

    //get balance
    @GetMapping("{userId}/info")
    public ResponseEntity<?> getWalletInfo(@PathVariable Long userId){
        Optional<User> userOptional = userService.getUserById(userId);
        User user = userOptional.orElse(null);
        Optional<Wallet> wallet = walletService.getWalletByUser(user);
        return ResponseEntity.ok(wallet);
    }

    //Deposit
    @PutMapping("{walletId}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable Long walletId, @RequestParam Double amount){
        return ResponseEntity.ok(walletService.deposit(walletId,amount));
    }

    //withdraw
    @PutMapping("{walletId}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable Long walletId, @RequestParam Double amount){
      return ResponseEntity.ok(walletService.withdraw(walletId,amount));
    }

    //get transaction history
    @GetMapping("{walletId}/transactions")
    public ResponseEntity<List<Transaction>> transactions(@PathVariable Long walletId){
        return ResponseEntity.ok(walletService.getTransactions(walletId));
    }

    //for communication
    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        // deduct money from wallet
        boolean success = walletService.processPayment(request.getUserId(), request.getAmount());
        if (success) {
            return new PaymentResponse(true, "Payment successful");
        } else {
            return new PaymentResponse(false, "Insufficient funds");
        }
    }



}
