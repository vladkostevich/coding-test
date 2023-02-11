package com.santander.fx.service;

import com.santander.fx.model.MarketPrice;

public interface MarketPriceService {

    MarketPrice getLatestPrice(String instrument);

    void updatePrice(MarketPrice marketPrice);
}
