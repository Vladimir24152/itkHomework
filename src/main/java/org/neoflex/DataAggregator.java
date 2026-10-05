package org.neoflex;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class DataAggregator {

    private static final int MIN_DELAY_MS = 1000;
    private static final int MAX_DELAY_MS = 3000;

    private static final int ERROR_PROBABILITY_PERCENT = 20;

    private static final String SERVICE_UNAVAILABLE_MSG = "Сервис временно недоступен";
    private static final String PRICE_ERROR_MSG = "Ошибка получения цены: ";
    private static final String DESCRIPTION_ERROR_MSG = "Ошибка получения описания: ";
    private static final String RATING_ERROR_MSG = "Ошибка получения рейтинга: ";

    private static final double DEFAULT_PRICE = 0.0;
    private static final String DEFAULT_DESCRIPTION = "Нет данных";
    private static final double DEFAULT_RATING = 0.0;

    private static final double MOCK_PRICE = 899.99;
    private static final String MOCK_DESCRIPTION = "Мощный ноутбук для работы и игр";
    private static final double MOCK_RATING = 4.7;

    private final Random random = new Random();

    private double fetchPrice(String productName) {
        sleep(MIN_DELAY_MS, MAX_DELAY_MS);
        maybeThrow();
        return MOCK_PRICE;
    }

    private String fetchDescription(String productName) {
        sleep(MIN_DELAY_MS, MAX_DELAY_MS);
        maybeThrow();
        return MOCK_DESCRIPTION;
    }

    private double fetchRating(String productName) {
        sleep(MIN_DELAY_MS, MAX_DELAY_MS);
        maybeThrow();
        return MOCK_RATING;
    }

    private void sleep(int minMs, int maxMs) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(minMs, maxMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void maybeThrow() {
        if (random.nextInt(100) < ERROR_PROBABILITY_PERCENT) {
            throw new RuntimeException(SERVICE_UNAVAILABLE_MSG);
        }
    }

    public ProductInfo aggregateProductInfo(String productName) {
        CompletableFuture<Double> priceFuture = CompletableFuture
                .supplyAsync(() -> fetchPrice(productName))
                .exceptionally(ex -> {
                    System.out.println(PRICE_ERROR_MSG + ex.getMessage());
                    return DEFAULT_PRICE;
                });

        CompletableFuture<String> descriptionFuture = CompletableFuture
                .supplyAsync(() -> fetchDescription(productName))
                .exceptionally(ex -> {
                    System.out.println(DESCRIPTION_ERROR_MSG + ex.getMessage());
                    return DEFAULT_DESCRIPTION;
                });

        CompletableFuture<Double> ratingFuture = CompletableFuture
                .supplyAsync(() -> fetchRating(productName))
                .exceptionally(ex -> {
                    System.out.println(RATING_ERROR_MSG + ex.getMessage());
                    return DEFAULT_RATING;
                });

        CompletableFuture<ProductInfo> resultFuture = priceFuture
                .thenCombine(descriptionFuture, (price, description) ->
                        new Object[]{price, description})
                .thenCombine(ratingFuture, (arr, rating) ->
                        new ProductInfo(productName, (Double) arr[0], (String) arr[1], rating));

        return resultFuture.join();
    }
}