package com.sivalabs.mongodbdemo.api;

import com.sivalabs.mongodbdemo.domain.Product;
import com.sivalabs.mongodbdemo.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Product saveProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

}
