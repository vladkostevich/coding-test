package com.santander.fx.converter;

import com.santander.fx.dto.MarketPriceDto;
import com.santander.fx.model.MarketPrice;

public interface MarketPriceConverter {

    MarketPriceDto toDto(MarketPrice marketPrice);
}
