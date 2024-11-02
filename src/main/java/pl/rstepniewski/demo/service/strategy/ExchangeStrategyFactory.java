package pl.rstepniewski.demo.service.strategy;

import org.springframework.stereotype.Component;
import pl.rstepniewski.demo.exception.InvalidCurrentPairException;

import java.util.Map;
import java.util.HashMap;

@Component
public class ExchangeStrategyFactory {
    private final Map<String, ExchangeStrategy> strategies;
    private static final String STRATEGY_CLASS_SUFFIX = "ExchangeStrategy";
    private static final String CURRENCY_PAIR_SEPARATOR = "To";
    private static final String ERROR_INVALID_CURRENCY_PAIR = "Invalid currency pair";

    public ExchangeStrategyFactory(Map<String, ExchangeStrategy> strategies) {
        this.strategies = new HashMap<>();
        strategies.forEach((key, value) ->
                this.strategies.put(key.replace(STRATEGY_CLASS_SUFFIX, ""), value));
    }

    public ExchangeStrategy getStrategy(String currencyFrom, String currencyTo) {
        String key = currencyFrom.toUpperCase() + CURRENCY_PAIR_SEPARATOR + currencyTo.toUpperCase();
        ExchangeStrategy strategy = strategies.get(key);
        if (strategy == null) {
            throw new InvalidCurrentPairException(ERROR_INVALID_CURRENCY_PAIR);
        }
        return strategy;
    }
}