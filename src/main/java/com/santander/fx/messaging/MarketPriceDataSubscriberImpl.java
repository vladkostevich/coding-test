package com.santander.fx.messaging;

import com.santander.fx.exception.MessageParsingException;
import com.santander.fx.model.MarketPrice;
import com.santander.fx.service.MarketPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@RequiredArgsConstructor
@Component
public class MarketPriceDataSubscriberImpl implements MarketPriceDataSubscriber {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");

    private final MarketPriceService marketPriceService;

    @Override
    public void onMessage(String message) {
        String[] csvRows = message.split("\n");

        for (String csvRow : csvRows) {
            String[] columns = csvRow.split(",");

            if (columns.length != 5) {
                log.error("Wrong number of columns in a csv row. Number is {}. Row content is {}", columns.length, columns);
                continue;
            }

            try {
                MarketPrice marketPrice = parseCsvRowWithMarketPriceData(columns);
                marketPriceService.updatePrice(marketPrice);
            } catch (MessageParsingException e) {
                log.error("Failed to parse a csv row. Row content is {}", columns, e);
            }
        }
    }

    private MarketPrice parseCsvRowWithMarketPriceData(String[] csvRowColumns) throws MessageParsingException {
        long id;
        try {
            id = Long.parseLong(csvRowColumns[0].trim());
        } catch (NumberFormatException e) {
            throw new MessageParsingException(
                    String.format("Failed to parse an id. Value is %s", csvRowColumns[0]));
        }

        String instrument = csvRowColumns[1].trim();
        if (instrument.isBlank()) {
            throw new MessageParsingException("Instrument code is blank");
        }

        BigDecimal bid;
        try {
            bid = new BigDecimal(csvRowColumns[2].trim());
        } catch (NumberFormatException e) {
            throw new MessageParsingException(
                    String.format("Failed to parse an bid value. Value is %s", csvRowColumns[2]));
        }

        BigDecimal ask;
        try {
            ask = new BigDecimal(csvRowColumns[3].trim());
        } catch (NumberFormatException e) {
            throw new MessageParsingException(
                    String.format("Failed to parse an ask value. Value is %s", csvRowColumns[3]));
        }

        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(csvRowColumns[4].trim(), TIMESTAMP_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new MessageParsingException(
                    String.format("Failed to parse timestamp value. Value is %s", csvRowColumns[4]));
        }

        return new MarketPrice(id, instrument, bid, ask, timestamp);
    }
}
