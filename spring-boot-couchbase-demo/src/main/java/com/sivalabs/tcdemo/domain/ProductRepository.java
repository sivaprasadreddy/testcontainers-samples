package com.sivalabs.tcdemo.domain;

import org.springframework.data.couchbase.repository.CouchbaseRepository;

interface ProductRepository extends CouchbaseRepository<Product, String> {}
