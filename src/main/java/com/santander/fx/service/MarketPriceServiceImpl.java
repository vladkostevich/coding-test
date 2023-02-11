package com.santander.fx.service;

import com.santander.fx.exception.MarketPriceNotFoundException;
import com.santander.fx.model.MarketPrice;
import com.santander.fx.storage.MarketPriceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarketPriceServiceImpl implements MarketPriceService {

    private static final BigDecimal BID_ADJUSTMENT_MULTIPLIER = new BigDecimal("0.999");
    private static final BigDecimal ASK_ADJUSTMENT_MULTIPLIER = new BigDecimal("1.001");

    private final MarketPriceStorage marketPriceStorage;

    @Override
    public MarketPrice getLatestPrice(String instrument) {
        MarketPrice latestPrice = marketPriceStorage.getByInstrument(instrument);

        if (latestPrice == null) {
            throw new MarketPriceNotFoundException(
                    String.format("Market price is not found for instrument %s", instrument));
        }

        return latestPrice;
    }

    @Override
    public void updatePrice(MarketPrice marketPrice) {
        MarketPrice latestPrice = marketPriceStorage.getByInstrument(marketPrice.getInstrument());

        if (latestPrice != null && marketPrice.getTimestamp().compareTo(latestPrice.getTimestamp()) <= 0) {
            log.warn("Skipping market price update with timestamp {} because newer or same value is stored already",
                    marketPrice.getTimestamp());
            return;
        }

        BigDecimal adjustedBid = marketPrice.getBid().multiply(BID_ADJUSTMENT_MULTIPLIER);
        marketPrice.setBid(adjustedBid);
        BigDecimal adjustedAsk = marketPrice.getAsk().multiply(ASK_ADJUSTMENT_MULTIPLIER);
        marketPrice.setAsk(adjustedAsk);

        marketPriceStorage.put(marketPrice.getInstrument(), marketPrice);
    }
}
