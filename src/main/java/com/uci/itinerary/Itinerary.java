package com.uci.itinerary;

import java.util.Date;

public class Itinerary {
	
	private Date date;
	private Route route;

	public Itinerary() {
		// TODO Auto-generated constructor stub
	}

	public Itinerary(Date date, Route route) {
		this.date = date;
		this.route = route;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
	
	

}
