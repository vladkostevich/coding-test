package com.santander.fx.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MarketPrice {
    private long id;
    private String instrument;
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDateTime timestamp;
}
