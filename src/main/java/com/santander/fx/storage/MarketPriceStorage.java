package com.santander.fx.storage;

import com.santander.fx.model.MarketPrice;

public interface MarketPriceStorage {

    void put(String instrument, MarketPrice marketPrice);

    MarketPrice getByInstrument(String instrument);
}
