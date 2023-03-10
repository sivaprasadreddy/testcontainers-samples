package demo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

	private final DBProvider dbProvider;

	public ProductService(DBProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

	public List<Product> getAll() throws SQLException {
		List<Product> products = new ArrayList<>();

		try (Connection conn = dbProvider.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("select id,name,description,price from products");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				BigDecimal price = rs.getBigDecimal("price");
				products.add(new Product(id, name, description, price));
			}
		}

		return products;
	}

}
