package com.sivalabs.productservice;

import com.sivalabs.productservice.domain.Product;
import com.sivalabs.productservice.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Product p1 = new Product(1L, "Samsung TV", new BigDecimal(45000));
        Product p2 = new Product(2L, "LG Fritz", new BigDecimal(25000));
        Product p3 = new Product(3L, "Lenovo Laptop", new BigDecimal(65000));
        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
    }
}
