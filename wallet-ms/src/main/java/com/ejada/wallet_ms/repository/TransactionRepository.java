package com.ejada.wallet_ms.repository;

import com.ejada.wallet_ms.bean.Transaction;
import com.ejada.wallet_ms.bean.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWallet(Wallet wallet);
}

