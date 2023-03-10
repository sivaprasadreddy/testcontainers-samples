package demo;

import java.sql.Connection;
import java.sql.DriverManager;

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
