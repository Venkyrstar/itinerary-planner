package com.uci.itinerary.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class PlaceDetail {
	
	private Place result;
	private String status;
	
	public Place getResult() {
		return result;
	}
	public void setResult(Place result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PlaceDetail [result=" + result + ", status=" + status + "]";
	}
	
	
	
}
