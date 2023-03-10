package com.sivalabs.tcdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
	@SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq")
	private Long id;

	@Column(nullable = false, unique = true)
	private String code;

	private String name;

	@Column(nullable = false)
	private BigDecimal price;

}
