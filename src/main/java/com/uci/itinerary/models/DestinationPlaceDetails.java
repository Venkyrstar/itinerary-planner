package com.uci.itinerary.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DestinationPlaceDetails {
	
	private List<Place> places;
	private DistanceMatrix distanceMatrix;
	
	public DestinationPlaceDetails() {
		
	}
	public List<Place> getPlaces() {
		return places;
	}
	public void setPlaces(List<Place> places) {
		this.places = places;
	}
	public DistanceMatrix getDistanceMatrix() {
		return distanceMatrix;
	}
	public void setDistanceMatrix(DistanceMatrix distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}
	
}
