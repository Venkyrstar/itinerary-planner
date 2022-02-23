package com.uci.itinerary.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DestinationPlaceDetails {
	
	private List<Place> places;
	private List<List<Metrics>> matrix;
	private String status;
	private String errorMessage;
	
	public DestinationPlaceDetails() {
		
	}
	public List<Place> getPlaces() {
		return places;
	}
	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	public List<List<Metrics>> getMatrix() {
		return matrix;
	}
	public void setMatrix(List<List<Metrics>> matrix) {
		this.matrix = matrix;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
