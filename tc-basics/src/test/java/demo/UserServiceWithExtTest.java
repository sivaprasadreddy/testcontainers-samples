package demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;

@Testcontainers
class UserServiceWithExtTest {

	static DBProvider dbProvider;

	UserService userService;

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2-alpine");

	@BeforeAll
	static void beforeAll() throws SQLException {
		dbProvider = new DBProvider("org.postgresql.Driver", postgres.getJdbcUrl(), postgres.getUsername(),
				postgres.getPassword());
		dbProvider.createUsersTable();
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