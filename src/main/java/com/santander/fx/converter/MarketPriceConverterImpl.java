package com.santander.fx.converter;

import com.santander.fx.dto.MarketPriceDto;
import com.santander.fx.model.MarketPrice;
import org.springframework.stereotype.Component;

@Component
public class MarketPriceConverterImpl implements MarketPriceConverter {

    @Override
    public MarketPriceDto toDto(MarketPrice marketPrice) {
        return new MarketPriceDto(
                marketPrice.getInstrument(),
                marketPrice.getBid(),
                marketPrice.getAsk(),
                marketPrice.getTimestamp()
        );
    }

}
