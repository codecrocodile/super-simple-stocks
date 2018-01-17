package com.codecrocodile.datastore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.codecrocodile.model.Stock;
import com.codecrocodile.model.StockType;
import com.codecrocodile.model.Trade;

public class SimpleDataStore implements DataStoreIF {
	
	private static Logger logger = Logger.getLogger(SimpleDataStore.class);
	
	private Map<String, Stock> stocks = new HashMap<>();
	
	private NavigableMap<Date, Trade> trades = new TreeMap<>();
	
	{
		this.stocks.put("TEA", new Stock("TEA", StockType.COMMON, new BigDecimal(0), null, new BigDecimal(100)));
		this.stocks.put("POP", new Stock("POP", StockType.COMMON, new BigDecimal(8), null, new BigDecimal(100)));
		this.stocks.put("ALE", new Stock("ALE", StockType.COMMON, new BigDecimal(23), null, new BigDecimal(60)));
		this.stocks.put("GIN", new Stock("GIN", StockType.PREFERRED, new BigDecimal(8), new BigDecimal(2), new BigDecimal(100)));
		this.stocks.put("JOE", new Stock("JOE", StockType.COMMON, new BigDecimal(13), null, new BigDecimal(250)));
	}
	

	public Stock getStock(String stockSymbol) {
		logger.trace(String.format("getStock(%s) ", stockSymbol));
		
		return this.stocks.get(stockSymbol);
	}
	
	public List<Stock> getAllStocks() {
		logger.trace("getAllStocks()");
		
		return new ArrayList<Stock>(this.stocks.values());
	}

	public void addTrade(Date date, Trade trade) {
		logger.trace(String.format("addTrade(%s, Trade trade) ", date.toString()));
		
		this.trades.put(date, trade);
	}

	public List<Trade> getTradesFromDate(String stockSymbol, Date fromDate) {
		logger.trace(String.format("getTradesFromDate(%s, %s)", stockSymbol, fromDate.toString()));
		
		ArrayList<Trade> tradesToReturn = new ArrayList<>();
		SortedMap<Date, Trade> tailMap = this.trades.tailMap(fromDate);
		for (Trade trade : tailMap.values()) {
			if (trade.getStockSymbol().equalsIgnoreCase(stockSymbol)) {
				tradesToReturn.add(trade);
			}
		}
		
		return tradesToReturn;
	}

	public Trade getLastTrade(String stockSymbol) {
		logger.trace(String.format("getLastTrade(%s)", stockSymbol));
		
		NavigableMap<Date,Trade> descendingMap = this.trades.descendingMap();
		for (Entry<Date, Trade> entry : descendingMap.entrySet()) {
			if (entry.getValue().getStockSymbol().equalsIgnoreCase(stockSymbol)) {
				return entry.getValue();
			}
		}
		
		return null;
	}

}
