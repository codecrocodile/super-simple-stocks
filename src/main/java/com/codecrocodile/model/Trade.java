package com.codecrocodile.model;

import java.math.BigDecimal;
import java.util.Date;

public class Trade {
	
	private String stockSymbol;
	
	private Date date;
	
	private int noOfShares;
	
	private Indicator indicator;
	
	private BigDecimal sharePrice;

	
	public Trade(String stockSymbol, Date date, int noOfShares, Indicator indicator, BigDecimal sharePrice) {
		super();
		this.stockSymbol = stockSymbol;
		this.date = date;
		this.noOfShares = noOfShares;
		this.indicator = indicator;
		this.sharePrice = sharePrice;
	}
	
	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNoOfShares() {
		return noOfShares;
	}

	public void setNoOfShares(int noOfShares) {
		this.noOfShares = noOfShares;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public BigDecimal getSharePrice() {
		return sharePrice;
	}

	public void setSharePrice(BigDecimal sharePrice) {
		this.sharePrice = sharePrice;
	}

}
