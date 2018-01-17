package com.codecrocodile.datastore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.codecrocodile.model.Indicator;
import com.codecrocodile.model.Stock;
import com.codecrocodile.model.Trade;

@RunWith(JUnit4.class)
public class SimpleDataStoreTest {
	
	private SimpleDataStore simpleDataStore;
	
	
	@Before
	public void setup() {
		simpleDataStore = new SimpleDataStore();
	}
	
	@After
	public void tearDown() {
		simpleDataStore = null;
	}
	
	@Test
	public void testGetStock() {
		String stockSymbol = "TEA";
		Stock stock = simpleDataStore.getStock(stockSymbol);
		assertThat(stock.getStockSymbol(), equalTo(stockSymbol));
	}
	
	@Test
	public void getAllStocks() throws Exception {
		List<Stock> allStocks = simpleDataStore.getAllStocks();
		
		Field declaredField = simpleDataStore.getClass().getDeclaredField("stocks");
		declaredField.setAccessible(true);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HashMap<String, Stock> stocks = (HashMap) declaredField.get(simpleDataStore);
		int actualNoOfStocks = stocks.size();
		
		assertThat(allStocks.size(), equalTo(actualNoOfStocks));
	}

	@Test
	public void addTrade() throws Exception {
		this.addTrades();
		
		Field declaredField = simpleDataStore.getClass().getDeclaredField("trades");
		declaredField.setAccessible(true);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		NavigableMap<Date, Trade> trades = (NavigableMap) declaredField.get(simpleDataStore);
		
		assertThat(trades.size(), equalTo(7));
	}

	@Test
	public void getTradesFromDate15Mins() {
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MINUTE, -15);
		
		this.addTrades();
		
		List<Trade> tradesSince = this.simpleDataStore.getTradesFromDate("TEA", nowCal.getTime());

		assertThat(tradesSince.size(), equalTo(3));
	}
	
	@Test
	public void getTradesFromDate30Mins() {
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MINUTE, -30);
		
		this.addTrades();
		
		List<Trade> tradesSince = this.simpleDataStore.getTradesFromDate("TEA", nowCal.getTime());

		assertThat(tradesSince.size(), equalTo(6));
	}
	
	@Test
	public void testGetLastTrade() {
		this.addTrades();
		
		Calendar nowCal = Calendar.getInstance();
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(111)));
		nowCal.add(Calendar.MINUTE, 2);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("MILK", nowCal.getTime(), 2000, Indicator.BUY, new BigDecimal(100)));
		
		Trade lastTrade = this.simpleDataStore.getLastTrade("TEA");
		assertThat(lastTrade.getStockSymbol(), equalTo("TEA"));
		assertThat(lastTrade.getSharePrice(), equalTo(new BigDecimal(111)));
		
	}
	
	private void addTrades() {
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MINUTE, -1);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
		nowCal.add(Calendar.MINUTE, -1);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
		nowCal.add(Calendar.MINUTE, -1);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
		nowCal.add(Calendar.MINUTE, -20);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
		nowCal.add(Calendar.MINUTE, -1);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
		nowCal.add(Calendar.MINUTE, -1);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
		nowCal.add(Calendar.MINUTE, -100);
		this.simpleDataStore.addTrade(nowCal.getTime(), new Trade("TEA", nowCal.getTime(), 5000, Indicator.BUY, new BigDecimal(105)));
	}

}
