package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBProvider {

	private final String driver;

	private final String url;

	private final String username;

	private final String password;

	public DBProvider(String driver, String url, String username, String password) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	void createUsersTable() throws SQLException {
		try (Connection conn = getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("""
					    create table users (
					        id bigint not null,
					        name varchar(100) not null,
					        email varchar(100) not null,
					        primary key (id),
					        CONSTRAINT email_unique UNIQUE (email)
					    )
					""");
			pstmt.execute();
		}
	}

	Connection getConnection() {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, username, password);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
