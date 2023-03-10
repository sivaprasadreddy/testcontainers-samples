package demo;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class UserServiceTest {

	static DBProvider dbProvider;

	UserService userService;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.2-alpine");

	@BeforeClass
	public static void beforeClass() throws SQLException {
		postgres.start();
		dbProvider = new DBProvider("org.postgresql.Driver", postgres.getJdbcUrl(), postgres.getUsername(),
				postgres.getPassword());
		dbProvider.createUsersTable();
	}

	@AfterClass
	public static void afterClass() {
		postgres.stop();
	}

	@BeforeMethod
	public void setUp() {
		userService = new UserService(dbProvider);
	}

	@Test
	public void shouldGetUsers() throws SQLException {
		userService.createUser(new User(1L, "Siva", "siva@gmail.com"));
		userService.createUser(new User(2L, "John", "john@gmail.com"));

		List<User> users = userService.getAllUsers();
		assertEquals(2, users.size());
	}

}