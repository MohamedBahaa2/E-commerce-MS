package com.ejada.shop_ms.service;

import com.ejada.shop_ms.bean.Product;
import com.ejada.shop_ms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProductService {
    @Autowired
    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Product createProduct(Product product){
        return productRepo.save(product);
    }

    public Product updateProduct(Long id, Product product){
        Product current = productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product not found!"));
        current.setName(product.getName());
        current.setDescription(product.getDescription());
        current.setPrice(product.getPrice());
        return productRepo.save(current);

    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

}
