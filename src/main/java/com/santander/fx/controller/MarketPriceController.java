package com.santander.fx.controller;

import com.santander.fx.converter.MarketPriceConverter;
import com.santander.fx.dto.MarketPriceDto;
import com.santander.fx.model.MarketPrice;
import com.santander.fx.service.MarketPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prices")
public class MarketPriceController {

    private final MarketPriceService marketPriceService;
    private final MarketPriceConverter marketPriceConverter;

    @GetMapping("/{instrument}")
    public MarketPriceDto getLatestPrice(@PathVariable String instrument) {
        MarketPrice latestPrice = marketPriceService.getLatestPrice(instrument);

        return marketPriceConverter.toDto(latestPrice);
    }
}
