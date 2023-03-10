package com.sivalabs.productservice.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sivalabs.productservice.config.ApplicationProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceClient {

	private static final String BACKEND_PROMOTION_SERVICE = "promotionService";

	private final TimeLimiterRegistry timeLimiterRegistry;

	private final RestTemplate restTemplate;

	private final ApplicationProperties properties;

	@Retry(name = BACKEND_PROMOTION_SERVICE, fallbackMethod = "fallback")
	@CircuitBreaker(name = BACKEND_PROMOTION_SERVICE)
	// @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name =
	// BACKEND_PROMOTION_SERVICE, fallbackMethod = "fallback")
	public List<Promotion> getProductPromotions() {
		try {
			List<Promotion> promotions = getTimeLimiter()
				.executeFutureSupplier(() -> CompletableFuture.supplyAsync(this::getPromotions));
			log.info("Promotions count: {} ", promotions.size());
			return promotions;

		}
		catch (Exception e) {
			log.error("---------BAM!!!!------");
			throw new RuntimeException(e);
		}
	}

	@Retry(name = BACKEND_PROMOTION_SERVICE, fallbackMethod = "fallbackAsyncRetry")
	@CircuitBreaker(name = BACKEND_PROMOTION_SERVICE)
	// @io.github.resilience4j.timelimiter.annotation.TimeLimiter(name =
	// BACKEND_PROMOTION_SERVICE, fallbackMethod = "fallbackAsyncTimeout")
	@io.github.resilience4j.timelimiter.annotation.TimeLimiter(name = BACKEND_PROMOTION_SERVICE)
	public CompletableFuture<List<Promotion>> getProductPromotionsAsync() {
		return CompletableFuture.supplyAsync(this::getPromotions);
	}

	private List<Promotion> getPromotions() {
		log.info("Fetching promotions from {}", properties.promotionServiceUrl() + "/api/promotions");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<List<Promotion>> response = restTemplate.exchange(
				properties.promotionServiceUrl() + "/api/promotions", HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<>() {
				});
		return response.getBody();
	}

	private List<Promotion> fallback(Exception e) {
		log.error("---------fallback---------");
		log.error("Error while fetching promotions", e);
		return List.of();
	}

	private CompletableFuture<List<Promotion>> fallbackAsync(Exception e) {
		log.error("Error while fetching promotions", e);
		log.info("---------fallbackAsync---------");
		return CompletableFuture.completedFuture(List.of());
	}

	private CompletableFuture<List<Promotion>> fallbackAsyncRetry(Exception e) {
		log.error("---------fallbackAsyncRetry---------");
		log.error("Error while fetching promotions", e);
		return CompletableFuture.completedFuture(List.of());
	}

	CompletableFuture<List<Promotion>> fallbackAsyncTimeout(Exception e) {
		log.error("---------fallbackAsyncTimeout---------");
		log.error("Error while fetching promotions", e);
		return CompletableFuture.completedFuture(List.of());
	}

	private TimeLimiter getTimeLimiter() {
		/*
		 * TimeLimiterConfig config = TimeLimiterConfig.custom()
		 * .cancelRunningFuture(true) .timeoutDuration(Duration.ofMillis(500)) .build();
		 * TimeLimiterRegistry registry = TimeLimiterRegistry.ofDefaults(); return
		 * registry.timeLimiter(BACKEND_PROMOTION_SERVICE, config);
		 */
		return timeLimiterRegistry.timeLimiter(BACKEND_PROMOTION_SERVICE);
	}

}
