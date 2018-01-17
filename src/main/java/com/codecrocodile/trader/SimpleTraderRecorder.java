package com.codecrocodile.trader;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import com.codecrocodile.calculator.CalculatorIF;
import com.codecrocodile.datastore.DataStoreIF;
import com.codecrocodile.model.Indicator;
import com.codecrocodile.model.InvalidTradeValuesException;
import com.codecrocodile.model.Stock;
import com.codecrocodile.model.Trade;

public class SimpleTraderRecorder implements TraderRecorderIF {
	
	private static Logger logger = Logger.getLogger(SimpleTraderRecorder.class);
	
	private DataStoreIF dataStore;
	
	private CalculatorIF calculator;
	
	
	public SimpleTraderRecorder(DataStoreIF dataStore, CalculatorIF calculator) {
		this.dataStore = dataStore;
		this.calculator = calculator;
	}

	public void trade(String stockSymbol, int noOfShares, Indicator indicator, BigDecimal sharePrice) throws InvalidTradeValuesException {
		this.validateTradeValues(stockSymbol, noOfShares, indicator, sharePrice);
		
		logger.trace(String.format("Recording trade: %s : %d : %s : %f", stockSymbol, noOfShares, indicator, sharePrice));
		
		Date timestamp = new Date();
		Trade trade = new Trade(stockSymbol, timestamp, noOfShares, indicator, sharePrice);
		this.dataStore.addTrade(timestamp, trade);
		
		BigDecimal newStockPrice = this.calculator.calculateStockPrice(trade.getStockSymbol());
		Stock stock = this.dataStore.getStock(stockSymbol);
		stock.setStockPrice(newStockPrice);
	}
	
	private void validateTradeValues(String stockSymbol, int noOfShares, Indicator indicator, BigDecimal sharePrice) 
			throws InvalidTradeValuesException {
		
		if (stockSymbol == null || this.dataStore.getStock(stockSymbol) == null) {
			throw new InvalidTradeValuesException(String.format("No stock with the given symbol : %s", stockSymbol));
		}
		
		if (noOfShares < 1) {
			throw new InvalidTradeValuesException("You need to buy or sell at least one share");
		}
		
		if (indicator == null) {
			throw new InvalidTradeValuesException("You must indicate if the trade is a BUY or SELL");
		}
		
		if (sharePrice.equals(new BigDecimal(0))) {
			throw new InvalidTradeValuesException("Trade must be off some value");
		}
		
	}

}
