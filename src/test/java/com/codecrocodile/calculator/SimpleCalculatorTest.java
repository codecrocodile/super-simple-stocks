package com.codecrocodile.calculator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codecrocodile.datastore.DataStoreIF;
import com.codecrocodile.model.Indicator;
import com.codecrocodile.model.Stock;
import com.codecrocodile.model.StockType;
import com.codecrocodile.model.Trade;

@RunWith(MockitoJUnitRunner.class)
public class SimpleCalculatorTest {
	
	@Mock
	private DataStoreIF dataStoreMock;
	
	private SimpleCalculator simpleCalculator;
	
	@Before
	public void setup() {
		this.simpleCalculator = new SimpleCalculator(this.dataStoreMock);
	}
	
	@After
	public void tearDown() {
		this.simpleCalculator = null;
	}
	
	/**
	 * Last Dividend / Stock Price
	 */
	@Test
	public void testCalculateDividendYieldCommonStockLowerBounds() {
		when(this.dataStoreMock.getStock("TEST1")).thenReturn(new Stock("TEST1", StockType.COMMON, new BigDecimal(0), null, new BigDecimal(200)));
		
		// if last dividend was zero then the yield was zero
		BigDecimal dividendYield1 = this.simpleCalculator.calculateDividendYield("TEST1");
		assertThat(dividendYield1, equalTo(new BigDecimal(0)));
	}
	
	/**
	 * Last Dividend / Stock Price
	 */
	@Test
	public void testCalculateDividendYieldCommonStock() {
		when(this.dataStoreMock.getStock("TEST2")).thenReturn(new Stock("TEST2", StockType.COMMON, new BigDecimal(20), null, new BigDecimal(200)));
		
		// if last dividend was 20 then the yield was 10%
		BigDecimal dividendYield2 = this.simpleCalculator.calculateDividendYield("TEST2");
		assertThat(dividendYield2, equalTo(new BigDecimal(0.1).round(new MathContext(1, RoundingMode.HALF_UP))));
	}
	
	/**
	 * Last Dividend / Stock Price
	 */
	@Test
	public void testCalculateDividendYieldCommonStockUpperBounds() {
		when(this.dataStoreMock.getStock("TEST3")).thenReturn(new Stock("TEST3", StockType.COMMON, new BigDecimal(200), null, new BigDecimal(200)));
		
		// if last dividend was 200 then the yield was 100%
		BigDecimal dividendYield3 = this.simpleCalculator.calculateDividendYield("TEST3");
		assertThat(dividendYield3, equalTo(new BigDecimal(1)));
	}

	/**
	 * Fixed Dividend * Par value / Stock Price
	 */
	@Test
	public void testCalculateDividendYieldPreferredStockLowerBounds() {
		when(this.dataStoreMock.getStock("TEST1")).thenReturn(new Stock("TEST1", StockType.PREFERRED, new BigDecimal(0), new BigDecimal(0), new BigDecimal(200)));
		
		// if fixed dividend was zero then the yield was zero
		BigDecimal dividendYield1 = this.simpleCalculator.calculateDividendYield("TEST1");
		assertThat(dividendYield1, equalTo(new BigDecimal(0)));
	}
	
	/**
	 * Fixed Dividend * Par value / Stock Price
	 */
	@Test
	public void testCalculateDividendYieldPreferredStock() {
		when(this.dataStoreMock.getStock("TEST2")).thenReturn(new Stock("TEST2", StockType.PREFERRED, new BigDecimal(0), new BigDecimal(0.1), new BigDecimal(200)));
		
		// if fixed dividend was 10% then the yield was 10%
		BigDecimal dividendYield2 = this.simpleCalculator.calculateDividendYield("TEST2");
		assertThat(dividendYield2.round(new MathContext(1, RoundingMode.HALF_UP)), equalTo(new BigDecimal(0.1).round(new MathContext(1, RoundingMode.HALF_UP))));
	}
	
	/**
	 * Fixed Dividend * Par value / Stock Price
	 */
	@Test
	public void testCalculateDividendYieldPreferredStockUpperBounds() {
		when(this.dataStoreMock.getStock("TEST3")).thenReturn(new Stock("TEST3", StockType.PREFERRED, new BigDecimal(0), new BigDecimal(1), new BigDecimal(200)));
		
		// if fixed dividend was 100% then the yield was 100%
		BigDecimal dividendYield3 = this.simpleCalculator.calculateDividendYield("TEST3");
		assertThat(dividendYield3, equalTo(new BigDecimal(1)));
	}

	/**
	 * Stock Price / Dividend
	 */
	@Test
	public void testCalculatePeRatioLowerBounds() {
		when(this.dataStoreMock.getStock("TEST1")).thenReturn(new Stock("TEST1", StockType.COMMON, new BigDecimal(0), null, new BigDecimal(200)));
		
		// if dividend is zero then profit to earnings ratio is 0
		BigDecimal peRatio1 = this.simpleCalculator.calculatePeRatio("TEST1");
		assertThat(peRatio1, equalTo(new BigDecimal(0)));
	}
	
	/**
	 * Stock Price / Dividend
	 */
	@Test
	public void testCalculatePeRatio() {
		when(this.dataStoreMock.getStock("TEST2")).thenReturn(new Stock("TEST2", StockType.COMMON, new BigDecimal(20), null, new BigDecimal(200)));
		
		// if dividend 10% of stock price then profit to earnings ratio is 10
		BigDecimal peRatio2 = this.simpleCalculator.calculatePeRatio("TEST2");
		assertThat(peRatio2, equalTo(new BigDecimal(10)));
	}

	/**
	 * Stock Price / Dividend
	 */
	@Test
	public void testCalculatePeRatioUpperBounds() {
		when(this.dataStoreMock.getStock("TEST3")).thenReturn(new Stock("TEST3", StockType.COMMON, new BigDecimal(200), null, new BigDecimal(200)));
		
		// if dividend is same as stock price then profit to earnings ratio is 1
		BigDecimal peRatio3 = this.simpleCalculator.calculatePeRatio("TEST3");
		assertThat(peRatio3, equalTo(new BigDecimal(1)));
	}
	
	/**
	 * Test that if no shares have been traded then the calculated stock price is the same as the par
	 * value off the stock. 
	 */
	@Test
	public void testCalculateStockPriceNoTradedShares() {
		String testStockSymbol = "TEST1";
		double parValue = 200d;
		
		when(this.dataStoreMock.getAllStocks()).thenReturn(new ArrayList<Stock>());
		when(this.dataStoreMock.getLastTrade(testStockSymbol)).thenReturn(null);
		when(this.dataStoreMock.getStock(testStockSymbol)).thenReturn(new Stock(testStockSymbol, StockType.COMMON, new BigDecimal(8), null, new BigDecimal(parValue)));
		
		BigDecimal stockPrice = this.simpleCalculator.calculateStockPrice(testStockSymbol);
		assertThat(stockPrice, equalTo(new BigDecimal(parValue)));
	}
	
	/**
	 * Test that if no shares have been traded in the last 15 minutes then the calculated share price
	 * is that off the last trade price.
	 */
	@Test
	public void testCalculateStockPriceLastTraded() {
		String testStockSymbol = "TEST1";
		Calendar nowCal = Calendar.getInstance();
		nowCal.add(Calendar.MINUTE, -20);
		double sharePrice = 200d;
		
		when(this.dataStoreMock.getAllStocks()).thenReturn(new ArrayList<Stock>());
		when(this.dataStoreMock.getLastTrade(testStockSymbol)).thenReturn(new Trade(testStockSymbol, nowCal.getTime(), 50, Indicator.BUY, new BigDecimal(sharePrice)));
		
		BigDecimal stockPrice = this.simpleCalculator.calculateStockPrice(testStockSymbol);
		assertThat(stockPrice, equalTo(new BigDecimal(sharePrice)));
	}
	
	/**
	 * Test that the share price calculation as follows is correct:
	 * 
	 * Sum (Trade Price * Number of shares) / Sum (Number of shares)
	 */
	@Test
	public void testCalculateStockPrice() {
		String testStockSymbol = "TEST1";
		Date nowDate = new Date();
		List<Trade> tradesInLast15Mins = new ArrayList<>();
		tradesInLast15Mins.add(new Trade(testStockSymbol, nowDate, 1, Indicator.BUY, new BigDecimal(100)));
		tradesInLast15Mins.add(new Trade(testStockSymbol, nowDate, 1, Indicator.BUY, new BigDecimal(110)));
		tradesInLast15Mins.add(new Trade(testStockSymbol, nowDate, 1, Indicator.BUY, new BigDecimal(100)));
		tradesInLast15Mins.add(new Trade(testStockSymbol, nowDate, 1, Indicator.BUY, new BigDecimal(110)));
		
		when(this.dataStoreMock.getTradesFromDate(Matchers.eq(testStockSymbol), Matchers.any(Date.class))).thenReturn(tradesInLast15Mins);
		
		BigDecimal stockPrice = this.simpleCalculator.calculateStockPrice(testStockSymbol);
		assertThat(stockPrice, equalTo(new BigDecimal(105)));
	}
	
	/**
	 * Test Geometric Mean calculation = nth root of the sum of n stock trading prices
	 * 
	 * Results where validated against: https://www.easycalculation.com/statistics/geometric-mean.php
	 */
	@Test
	public void testCalculateAllShareIndex() {
		List<Stock> allStocks = new ArrayList<>();
		allStocks.add(new Stock("TEST1", StockType.COMMON, new BigDecimal(5), null, new BigDecimal(100)));
		allStocks.add(new Stock("TEST2", StockType.COMMON, new BigDecimal(5), null, new BigDecimal(100)));
		allStocks.add(new Stock("TEST3", StockType.COMMON, new BigDecimal(5), null, new BigDecimal(100)));
		allStocks.add(new Stock("TEST4", StockType.COMMON, new BigDecimal(5), null, new BigDecimal(600)));
		
		when(this.dataStoreMock.getAllStocks()).thenReturn(allStocks);
		BigDecimal allShareIndex = this.simpleCalculator.calculateAllShareIndex();
		System.out.println(allShareIndex);
		assertThat(allShareIndex.round(new MathContext(4, RoundingMode.HALF_UP)), equalTo(new BigDecimal(156.5)));
	}

}
