package com.sivalabs.tcdemo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.disabled=false ")
    List<Product> findAllActiveProducts();
}
