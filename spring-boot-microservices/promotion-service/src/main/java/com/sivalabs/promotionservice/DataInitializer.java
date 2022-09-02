package com.sivalabs.promotionservice;

import com.sivalabs.promotionservice.domain.Promotion;
import com.sivalabs.promotionservice.domain.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final PromotionRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Promotion p1 = new Promotion(1L, 1L, BigDecimal.TEN);
        Promotion p2 = new Promotion(2L, 2L, BigDecimal.ONE);
        repository.save(p1);
        repository.save(p2);
    }
}
