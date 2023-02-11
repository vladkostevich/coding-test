package com.santander.fx.messaging;

import com.santander.fx.model.MarketPrice;
import com.santander.fx.service.MarketPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.then;
import static org.mockito.internal.verification.VerificationModeFactory.only;

@ExtendWith(MockitoExtension.class)
class MarketPriceDataSubscriberImplTest {

    MarketPriceDataSubscriberImpl sut;

    @Mock
    MarketPriceService marketPriceService;

    @BeforeEach
    void setUp() {
        sut = new MarketPriceDataSubscriberImpl(marketPriceService);
    }

    @Test
    void onMessage_shouldUpdatedPricesFromCsvData_whenInputIsValid() {
        // Given
        String message = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedFirstRowPrice = new MarketPrice(
                106L, "EUR/USD", new BigDecimal("1.1000"), new BigDecimal("1.2000"), LocalDateTime.parse("2020-06-01T12:01:01.001"));
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should().updatePrice(expectedFirstRowPrice);
        then(marketPriceService).should().updatePrice(expectedSecondRowPrice);
    }

    @Test
    void onMessage_shouldSkipRow_whenRowContainsUnexpectedNumberOfColumns() {
        // Given
        String message = "106, EUR/USD, 1.1000,\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should(only()).updatePrice(expectedSecondRowPrice);
    }

    @Test
    void onMessage_shouldSkipRow_whenRowContainsInvalidId() {
        // Given
        String message = "106FD, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should(only()).updatePrice(expectedSecondRowPrice);
    }

    @Test
    void onMessage_shouldSkipRow_whenRowContainsBlankInstrumentName() {
        // Given
        String message = "106,   , 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should(only()).updatePrice(expectedSecondRowPrice);
    }

    @Test
    void onMessage_shouldSkipRow_whenRowContainsInvalidBidValue() {
        // Given
        String message = "106, EUR/USD, 1.1000QEE4,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should(only()).updatePrice(expectedSecondRowPrice);
    }

    @Test
    void onMessage_shouldSkipRow_whenRowContainsInvalidAskValue() {
        // Given
        String message = "106, EUR/USD, 1.1000,1.2000klf43,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should(only()).updatePrice(expectedSecondRowPrice);
    }

    @Test
    void onMessage_shouldSkipRow_whenRowContainsInvalidTimestamp() {
        // Given
        String message = "106, EUR/USD, 1.1000,1.2000,01-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002";
        MarketPrice expectedSecondRowPrice = new MarketPrice(
                107L, "EUR/JPY", new BigDecimal("119.60"), new BigDecimal("119.90"), LocalDateTime.parse("2020-06-01T12:01:02.002"));

        // When
        sut.onMessage(message);

        // Then
        then(marketPriceService).should(only()).updatePrice(expectedSecondRowPrice);
    }
}