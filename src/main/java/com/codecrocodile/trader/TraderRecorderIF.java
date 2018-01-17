package com.codecrocodile.trader;

import java.math.BigDecimal;

import com.codecrocodile.model.Indicator;
import com.codecrocodile.model.InvalidTradeValuesException;

public interface TraderRecorderIF {
	
	void trade(String stockSymbol, int noOfShares, Indicator indicator, BigDecimal sharePrice) throws InvalidTradeValuesException;

}
