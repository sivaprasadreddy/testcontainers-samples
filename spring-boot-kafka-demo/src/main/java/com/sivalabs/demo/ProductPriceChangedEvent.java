package com.sivalabs.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductPriceChangedEvent {

    private String productCode;

    private BigDecimal price;

    private LocalDateTime eventDate;
}
