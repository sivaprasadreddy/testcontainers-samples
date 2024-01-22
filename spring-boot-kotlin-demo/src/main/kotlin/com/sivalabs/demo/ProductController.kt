package com.sivalabs.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(private val repository: ProductRepository) {
    @GetMapping
    fun getAll() = repository.getAll()
}
