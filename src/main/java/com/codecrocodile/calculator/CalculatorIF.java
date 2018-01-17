package com.codecrocodile.calculator;

import java.math.BigDecimal;

public interface CalculatorIF {
	
	BigDecimal calculateDividendYield(String stockSymbol);
	
	BigDecimal calculatePeRatio(String stockSymbol);
	
	BigDecimal calculateStockPrice(String stockSymbol);
	
	BigDecimal calculateAllShareIndex();

}
