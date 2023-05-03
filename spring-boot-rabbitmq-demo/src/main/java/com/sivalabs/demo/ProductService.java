package com.sivalabs.demo;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public void updateProductPrice(String productCode, BigDecimal price) {
        repository.updateProductPrice(productCode, price);
    }
}
