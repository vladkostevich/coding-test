package com.santander.fx.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class MarketPriceDto {
    String instrument;
    BigDecimal bid;
    BigDecimal ask;
    LocalDateTime timestamp;
}
