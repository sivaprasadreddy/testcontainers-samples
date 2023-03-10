package com.sivalabs;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresResource implements QuarkusTestResourceLifecycleManager {

	static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15.2-alpine");

	@Override
	public Map<String, String> start() {
		db.start();
		return Map.of("%test.quarkus.datasource.jdbc.url", db.getJdbcUrl(), "%test.quarkus.datasource.username",
				db.getUsername(), "%test.quarkus.datasource.password", db.getPassword());
	}

	@Override
	public void stop() {
		db.stop();
	}

}