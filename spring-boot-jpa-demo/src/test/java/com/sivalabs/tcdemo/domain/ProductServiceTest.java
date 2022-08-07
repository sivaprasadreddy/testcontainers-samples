package com.sivalabs.tcdemo.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldReturnOnlyActiveProducts() {
        //Arrange
        Product p1 = new Product(1L, "p-name1", "p-desc1", BigDecimal.TEN, false);
        Product p2 = new Product(2L, "p-name2", "p-desc2", BigDecimal.TEN, true);
        BDDMockito.given(productRepository.findAll()).willReturn(List.of(p1, p2));


        //Act
        List<Product> products = productService.getAllProducts();

        //Assert
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getId()).isEqualTo(1L);
    }
}