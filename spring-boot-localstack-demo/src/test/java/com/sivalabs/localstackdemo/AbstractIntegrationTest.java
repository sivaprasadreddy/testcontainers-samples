package com.sivalabs.localstackdemo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfig.class)
abstract class AbstractIntegrationTest {}
