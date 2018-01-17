package com.codecrocodile.trade;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codecrocodile.calculator.CalculatorIF;
import com.codecrocodile.datastore.DataStoreIF;
import com.codecrocodile.model.Indicator;
import com.codecrocodile.model.InvalidTradeValuesException;
import com.codecrocodile.model.Stock;
import com.codecrocodile.model.StockType;
import com.codecrocodile.model.Trade;
import com.codecrocodile.trader.SimpleTraderRecorder;


@RunWith(MockitoJUnitRunner.class)
public class SimpleTradeRecorderTest {

	@Mock
	private DataStoreIF dataStore;

	@Mock
	private CalculatorIF calculator;
	
	private SimpleTraderRecorder simpleTraderRecorder;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	
	@Before
	public void setup() {
		simpleTraderRecorder = new SimpleTraderRecorder(dataStore, calculator);
	}
	
	@After
	public void tearDown() {
		simpleTraderRecorder = null;
	}
	
	@Test
	public void testInvalidStockSymbol() throws InvalidTradeValuesException {
	    exception.expect(InvalidTradeValuesException.class);
	    simpleTraderRecorder.trade("FAIL", 0, null, new BigDecimal(0));
	}
	
	@Test
	public void testInvalidOnOfShares() throws InvalidTradeValuesException {
	    exception.expect(InvalidTradeValuesException.class);
	    simpleTraderRecorder.trade("TEA", 0, null, new BigDecimal(0));
	}
	
	@Test
	public void testInvalidIndicator() throws InvalidTradeValuesException {
		exception.expect(InvalidTradeValuesException.class);
		simpleTraderRecorder.trade("TEA", 1, null, new BigDecimal(0));
	}
	
	@Test
	public void testInvalidSharePrice() throws InvalidTradeValuesException {
		exception.expect(InvalidTradeValuesException.class);
		simpleTraderRecorder.trade("TEA", 0, Indicator.SELL, new BigDecimal(0));
	}
	
	/**
	 * Test that when recording a trade it is stored in the data source then then new stock
	 * price is calculated in that order.
	 *  
	 * @throws InvalidTradeValuesException
	 */
	@Test
	public void testTrade() throws InvalidTradeValuesException {
		String stockSymbol = "TEST";
		when(this.dataStore.getStock(stockSymbol)).thenReturn(new Stock(stockSymbol, StockType.COMMON, new BigDecimal(5), null, new BigDecimal(200)));
		when(this.calculator.calculateStockPrice(stockSymbol)).thenReturn(new BigDecimal(205));
		
		this.simpleTraderRecorder.trade(stockSymbol, 5, Indicator.BUY, new BigDecimal(203));
		
		InOrder inOrder = Mockito.inOrder(this.dataStore, this.calculator);
		inOrder.verify(this.dataStore).addTrade(Mockito.any(Date.class), Mockito.any(Trade.class));
		inOrder.verify(this.calculator).calculateStockPrice(stockSymbol);
	}
	
}
