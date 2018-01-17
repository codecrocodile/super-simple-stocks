package com.codecrocodile.datastore;

import java.util.Date;
import java.util.List;

import com.codecrocodile.model.Stock;
import com.codecrocodile.model.Trade;

public interface DataStoreIF {

	Stock getStock(String stockSymbol);
	
	List<Stock> getAllStocks();
	
	void addTrade(Date date, Trade trade);
	
	List<Trade> getTradesFromDate(String stockSymbol, Date date);
	
	Trade getLastTrade(String stockSymbol);
	
}
