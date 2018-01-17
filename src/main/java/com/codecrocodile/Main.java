package com.codecrocodile;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import com.codecrocodile.calculator.CalculatorIF;
import com.codecrocodile.calculator.SimpleCalculator;
import com.codecrocodile.datastore.DataStoreIF;
import com.codecrocodile.datastore.SimpleDataStore;
import com.codecrocodile.model.Indicator;
import com.codecrocodile.model.InvalidTradeValuesException;
import com.codecrocodile.model.Trade;
import com.codecrocodile.trader.SimpleTraderRecorder;

public class Main {
	
	private static final Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) {
		DataStoreIF dataStore = new SimpleDataStore();
		CalculatorIF calculator = new SimpleCalculator(dataStore);
		SimpleTraderRecorder simpleTrader = new SimpleTraderRecorder(dataStore, calculator);
		
		BigDecimal calculateAllShareIndex = calculator.calculateAllShareIndex();
		logger.info(calculateAllShareIndex.toString());
		
		BigDecimal calculateDividendYield = calculator.calculateDividendYield("TEA");
		logger.info(calculateDividendYield.toString());
		
		BigDecimal calculatePeRatio = calculator.calculatePeRatio("TEA");
		logger.info(calculatePeRatio.toString());
		
		Trade firstTrade = new Trade("TEA", new Date(), 100, Indicator.BUY, new BigDecimal(105));
		BigDecimal calculateStockPrice = calculator.calculateStockPrice(firstTrade.getStockSymbol());
		logger.info(calculateStockPrice.toString());
		
	
		try {
			simpleTrader.trade("TEA", 100, Indicator.BUY, new BigDecimal(105));
		} catch (InvalidTradeValuesException e) {
			e.printStackTrace();
		}
	}

}
