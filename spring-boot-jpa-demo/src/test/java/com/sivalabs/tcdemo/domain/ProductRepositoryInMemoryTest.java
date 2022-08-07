package com.sivalabs.tcdemo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryInMemoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        entityManager.persist(new Product(null, "pname1", "pdescr1", BigDecimal.TEN, false));
        entityManager.persist(new Product(null, "pname2", "pdescr2", BigDecimal.TEN, true));
    }

    @Test
    void shouldGetAllActiveProducts() {
        List<Product> products = productRepository.findAllActiveProducts();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("pname1");
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
    }
}