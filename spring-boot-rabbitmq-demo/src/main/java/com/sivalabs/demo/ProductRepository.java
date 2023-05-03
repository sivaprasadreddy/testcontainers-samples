package com.sivalabs.demo;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("update Product p set p.price = :price where p.code = :productCode")
    @Modifying
    void updateProductPrice(@Param("productCode") String productCode, @Param("price") BigDecimal price);

    Optional<Product> findByCode(String code);
}
