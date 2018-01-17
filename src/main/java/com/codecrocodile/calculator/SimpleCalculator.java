package com.codecrocodile.calculator;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.codecrocodile.datastore.DataStoreIF;
import com.codecrocodile.model.Stock;
import com.codecrocodile.model.Trade;

public class SimpleCalculator implements CalculatorIF {
	
	private static Logger logger = Logger.getLogger(SimpleCalculator.class);
	
	private DataStoreIF dataStore;

	
	public SimpleCalculator(DataStoreIF dataStore) {
		super();
		this.dataStore = dataStore;
	}

	public BigDecimal calculateDividendYield(String stockSymbol) {
		logger.trace(String.format("calculateDividendYield(%s) ", stockSymbol));
		
		BigDecimal dividendYield = new BigDecimal(0);
		Stock stock = this.dataStore.getStock(stockSymbol);
		
		switch (stock.getStockType()) {
		case COMMON:
			if (!stock.getLastDividend().equals(new BigDecimal(0))) {
				dividendYield = stock.getLastDividend().divide(stock.getStockPrice());
			}
			break;
		case PREFERRED:
			dividendYield = (stock.getFixedDividend().multiply(stock.getParValue())).divide(stock.getStockPrice());
			break;
		}
		
		return dividendYield;
	}

	public BigDecimal calculatePeRatio(String stockSymbol) {
		logger.trace(String.format("calculatePeRatio(%s) ", stockSymbol));
		
		Stock stock = this.dataStore.getStock(stockSymbol);
		if (stock.getLastDividend().equals(new BigDecimal(0))) {
			return new BigDecimal(0);
		} else {
			return stock.getStockPrice().divide(stock.getLastDividend());
		}
	}

	public BigDecimal calculateStockPrice(String stockSymbol) {
		logger.trace(String.format("calculateStockPrice(%s) ", stockSymbol));
		
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MINUTE, -15);
		
		List<Trade> allTrades = this.dataStore.getTradesFromDate(stockSymbol, nowCal.getTime());
		
		if (allTrades.size() == 0) {
			Trade lastTrade = this.dataStore.getLastTrade(stockSymbol);
			if (lastTrade != null) {
				return lastTrade.getSharePrice();	
			} else {
				Stock stock = this.dataStore.getStock(stockSymbol);
				return stock.getParValue();
			}
		}
		
		BigDecimal productSum = new BigDecimal(0);
		BigDecimal product = null;
		int noOfSharesSum = 0;
		for (Trade trade : allTrades) {
			product = trade.getSharePrice().multiply(new BigDecimal(trade.getNoOfShares()));
			productSum = productSum.add(product);
			noOfSharesSum += trade.getNoOfShares();
		}
		
		return (productSum.divide(new BigDecimal(noOfSharesSum)));
	}
	
	public BigDecimal calculateAllShareIndex() {
		logger.trace("calculateAllShareIndex()");
		
		List<Stock> allStocks = this.dataStore.getAllStocks();
		int count = 0;
		BigDecimal product = new BigDecimal(1);
		for (Stock s : allStocks) {
			count++;
			product = product.multiply(s.getStockPrice());
		}
		
		double ans = this.nthroot(count, product.doubleValue());
		
		return new BigDecimal(ans);
	}
	
	/*
	 * Algorithm obtained from http://rosettacode.org/wiki/Nth_root#Java
	 */
	private double nthroot(int n, double A) {
		return nthroot(n, A, .001);
	}

	/*
	 * Algorithm obtained from http://rosettacode.org/wiki/Nth_root#Java
	 */
	private double nthroot(int n, double A, double p) {
		if (A < 0) {
			return -1;
		} else if (A == 0) {
			return 0;
		}
		double x_prev = A;
		double x = A / n; // starting "guessed" value...
		while (Math.abs(x - x_prev) > p) {
			x_prev = x;
			x = ((n - 1.0) * x + A / Math.pow(x, n - 1.0)) / n;
		}
		return x;
	}

}
