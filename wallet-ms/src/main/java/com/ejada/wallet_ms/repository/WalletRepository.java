package com.ejada.wallet_ms.repository;

import com.ejada.wallet_ms.bean.User;
import com.ejada.wallet_ms.bean.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByUser(User user);
}
