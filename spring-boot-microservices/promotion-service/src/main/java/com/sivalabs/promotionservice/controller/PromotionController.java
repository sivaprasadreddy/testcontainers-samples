package com.sivalabs.promotionservice.controller;

import com.sivalabs.promotionservice.domain.Promotion;
import com.sivalabs.promotionservice.domain.PromotionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PromotionController {
    private final PromotionRepository promotionRepository;

    @GetMapping("/api/promotions")
    public List<Promotion> getPromotions() {
        //randomWait();
        return promotionRepository.findAll();
    }

    private void randomWait() {
        int waitSeconds = getRandomNumber(0,3);
        log.info("Sleeping for {} seconds", waitSeconds);
        try {
            Thread.sleep(waitSeconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
