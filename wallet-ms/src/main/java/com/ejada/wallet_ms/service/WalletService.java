package com.ejada.wallet_ms.service;

import com.ejada.wallet_ms.bean.Transaction;
import com.ejada.wallet_ms.bean.User;
import com.ejada.wallet_ms.bean.Wallet;
import com.ejada.wallet_ms.repository.TransactionRepository;
import com.ejada.wallet_ms.repository.UserRepository;
import com.ejada.wallet_ms.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    //repositories
    private final WalletRepository walletRepo;
    private final TransactionRepository txRepo;
    private final UserRepository userRepo;

    @Autowired
    public WalletService(WalletRepository walletRepo, TransactionRepository txRepo, UserRepository userRepo){
        this.walletRepo = walletRepo;
        this.txRepo = txRepo;
        this.userRepo = userRepo;
    }

    //wallet creation
    public Wallet createWallet(Long userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User with ID: "+userId+" not found!"));

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);

        return walletRepo.save(wallet);
    }

    //deposit
    @Transactional
    public Wallet deposit(Long walletId, double amount){
        Wallet wallet = walletRepo.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet with ID: "+walletId+" not found!"));

        wallet.setBalance(wallet.getBalance()+ amount);
        walletRepo.save(wallet);

        // save the transaction details
        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setAmount(amount);
        tx.setType(Transaction.TransactionType.DEPOSIT);

        txRepo.save(tx);

        return wallet;
    }

    //withdraw
    @Transactional
    public Wallet withdraw(Long walletId, double amount){
        Wallet wallet = walletRepo.findById(walletId).orElseThrow(()-> new RuntimeException("Wallet with ID: "+walletId+" not found!"));

        if (wallet.getBalance() < amount){
            throw new RuntimeException("Insufficient funds @"+walletId);
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);

        // save the transaction details
        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setAmount(amount);
        tx.setType(Transaction.TransactionType.WITHDRAWAL);

        txRepo.save(tx);


        return wallet;
    }

    //get transaction history for a wallet
    public List<Transaction> getTransactions(Long walletId){
        Wallet wallet = walletRepo.findById(walletId).orElseThrow(()-> new RuntimeException("Wallet with ID: "+walletId+" not found!"));

        return txRepo.findByWallet(wallet);
    }

    public Optional<Wallet> getWalletByUser(User user){
        Optional<Wallet> wallet = walletRepo.findByUser(user);
        return wallet;
    }


    public boolean processPayment(Long userId, Double amount) {
        Wallet wallet = walletRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));

        if (wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepo.save(wallet);
            return true; // payment successful
        } else {
            return false; // insufficient funds
        }
    }


}
