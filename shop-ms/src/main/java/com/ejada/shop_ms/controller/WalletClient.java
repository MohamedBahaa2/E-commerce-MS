package com.ejada.shop_ms.controller;

import com.ejada.shop_ms.DTO.PaymentRequest;
import com.ejada.shop_ms.DTO.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-ms") // service name registered in Eureka
public interface WalletClient {

    @PostMapping("/wallet/pay")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}