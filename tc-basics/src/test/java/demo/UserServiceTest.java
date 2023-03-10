package demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.List;

class UserServiceTest {

	static DBProvider dbProvider;

	UserService userService;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2-alpine");

	@BeforeAll
	static void beforeAll() throws SQLException {
		postgres.start();
		dbProvider = new DBProvider("org.postgresql.Driver", postgres.getJdbcUrl(), postgres.getUsername(),
				postgres.getPassword());
		dbProvider.createUsersTable();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@BeforeEach
	void setUp() {
		userService = new UserService(dbProvider);
	}

	@Test
	void shouldGetUsers() throws SQLException {
		userService.createUser(new User(1L, "Siva", "siva@gmail.com"));
		userService.createUser(new User(2L, "John", "john@gmail.com"));

		List<User> users = userService.getAllUsers();
		Assertions.assertEquals(2, users.size());
	}

}