package com.santander.fx.storage;

import com.santander.fx.model.MarketPrice;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketPriceInMemoryStorage implements MarketPriceStorage {

    private final Map<String, MarketPrice> marketPriceMap = new ConcurrentHashMap<>();

    @Override
    public void put(String code, MarketPrice marketPrice) {
        marketPriceMap.put(code, marketPrice);
    }

    @Override
    public MarketPrice getByInstrument(String code) {
        return marketPriceMap.get(code);
    }
}
