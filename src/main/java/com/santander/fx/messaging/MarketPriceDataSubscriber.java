package com.santander.fx.messaging;

public interface MarketPriceDataSubscriber {

    void onMessage(String message);
}
