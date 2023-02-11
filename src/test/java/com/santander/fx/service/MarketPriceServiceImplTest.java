package com.santander.fx.service;

import com.santander.fx.exception.MarketPriceNotFoundException;
import com.santander.fx.model.MarketPrice;
import com.santander.fx.storage.MarketPriceStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MarketPriceServiceImplTest {

    MarketPriceServiceImpl sut;

    @Mock
    MarketPriceStorage marketPriceStorage;

    @BeforeEach
    void setUp() {
        sut = new MarketPriceServiceImpl(marketPriceStorage);
    }

    @Test
    void getLatestPrice_shouldThrowException_whenNoDataFoundForInstrument() {
        // Given
        String instrument = "USD/EUR";

        given(marketPriceStorage.getByInstrument(instrument)).willReturn(null);

        // When
        assertThrows(MarketPriceNotFoundException.class, () -> sut.getLatestPrice(instrument));
    }

    @Test
    void getLatestPrice_shouldReturnLastValue_whenNoDataFoundForInstrument() {
        // Given
        String instrument = "USD/EUR";
        MarketPrice expectedPrice = new MarketPrice(
                123L, instrument, new BigDecimal("1.87"), new BigDecimal("1.91"), LocalDateTime.now());

        given(marketPriceStorage.getByInstrument(instrument)).willReturn(expectedPrice);

        // When
        MarketPrice actualPrice = sut.getLatestPrice(instrument);

        // Then
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void updatePrice_shouldDoNothing_whenThereIsNewerDataInStorage() {
        // Given
        String instrument = "USD/EUR";
        MarketPrice latestPriceInStorage = new MarketPrice(
                123L, instrument, new BigDecimal("1.87"), new BigDecimal("1.91"), LocalDateTime.now());
        MarketPrice passedPrice = new MarketPrice(
                122L, instrument, new BigDecimal("1.86"), new BigDecimal("1.90"), LocalDateTime.now().minusMinutes(1));

        given(marketPriceStorage.getByInstrument(instrument)).willReturn(latestPriceInStorage);

        // When
        sut.updatePrice(passedPrice);

        // Then
        then(marketPriceStorage).shouldHaveNoMoreInteractions();
    }

    @Test
    void updatePrice_shouldSaveAdjustedPrice_whenNewPriceDataIsPassed() {
        // Given
        String instrument = "USD/EUR";
        LocalDateTime currentTime = LocalDateTime.now();
        MarketPrice latestPriceInStorage = new MarketPrice(
                122L, instrument, new BigDecimal("1.86"), new BigDecimal("1.90"), currentTime.minusMinutes(1));
        MarketPrice passedPrice = new MarketPrice(
                123L, instrument, new BigDecimal("1.87"), new BigDecimal("1.91"), currentTime);
        MarketPrice expectedPrice = new MarketPrice(
                123L, instrument, new BigDecimal("1.86813"), new BigDecimal("1.91191"), currentTime);

        given(marketPriceStorage.getByInstrument(instrument)).willReturn(latestPriceInStorage);

        // When
        sut.updatePrice(passedPrice);

        // Then
        then(marketPriceStorage).should().put(instrument, expectedPrice);
    }
}