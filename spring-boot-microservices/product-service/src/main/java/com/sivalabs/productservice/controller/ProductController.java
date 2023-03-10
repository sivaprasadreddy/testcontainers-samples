package com.sivalabs.productservice.controller;

import com.sivalabs.productservice.domain.Product;
import com.sivalabs.productservice.domain.ProductResult;
import com.sivalabs.productservice.domain.Promotion;
import com.sivalabs.productservice.domain.ProductRepository;
import com.sivalabs.productservice.domain.PromotionServiceClient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductRepository productRepository;

	private final PromotionServiceClient promotionServiceClient;

	@GetMapping("/api/products")
	public List<ProductResult> getProducts() throws Exception {
		List<Product> products = productRepository.findAll();

		// List<Promotion> promotions = promotionServiceClient.getProductPromotions();
		List<Promotion> promotions = promotionServiceClient.getProductPromotionsAsync().get();
		Map<Long, Promotion> promotionsMap = promotions.stream()
			.collect(Collectors.toMap(Promotion::getProductId, promotion -> promotion));

		List<ProductResult> productResults = new ArrayList<>(products.size());
		for (Product product : products) {
			ProductResult productResult = new ProductResult();
			productResult.setId(product.getId());
			productResult.setName(product.getName());
			productResult.setOriginalPrice(product.getPrice());
			if (promotionsMap.containsKey(product.getId())) {
				Promotion promotion = promotionsMap.get(product.getId());
				productResult.setDiscount(promotion.getDiscount());
				productResult.setPrice(product.getPrice().subtract(promotion.getDiscount()));
			}
			else {
				productResult.setDiscount(BigDecimal.ZERO);
				productResult.setPrice(product.getPrice());
			}
			productResults.add(productResult);
		}
		return productResults;
	}

}
