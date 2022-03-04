package com.uci.itinerary.models;

public class TravelTime {
	private String start;
	private String end;
	public TravelTime(String start, String end) {
		super();
		this.start = start;
		this.end = end;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
}
