package com.sivalabs.mongodbdemo.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ProductRepository extends MongoRepository<Product, String> {

}
