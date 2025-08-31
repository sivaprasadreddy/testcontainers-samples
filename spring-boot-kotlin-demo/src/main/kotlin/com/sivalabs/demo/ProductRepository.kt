package com.sivalabs.demo

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun getAll(): List<Product> =
        jdbcTemplate.query(
            "select * from products",
        ) { rs, _ ->
            Product(
                rs.getLong("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
            )
        }
}
