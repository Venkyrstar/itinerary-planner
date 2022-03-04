package com.uci.itinerary.models;

public class Interval {
	
	private int open;
	private int close;
	
	public Interval(int open, int close) {
		super();
		this.open = open;
		this.close = close;
	}
	public int getOpen() {
		return open;
	}
	public void setOpen(int open) {
		this.open = open;
	}
	public int getClose() {
		return close;
	}
	public void setClose(int close) {
		this.close = close;
	}
	
}
