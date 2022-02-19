package com.uci.itinerary.models;

import java.util.ArrayList;

public class Route {
	
	private ArrayList<Place> places;
	private double ratingScore;
	private double distanceScore;
	private double profitScore;

	public Route() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}

	public double getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(double ratingScore) {
		this.ratingScore = ratingScore;
	}

	public double getDistanceScore() {
		return distanceScore;
	}

	public void setDistanceScore(double distanceScore) {
		this.distanceScore = distanceScore;
	}

	public double getProfitScore() {
		return profitScore;
	}

	public void setProfitScore(double profitScore) {
		this.profitScore = profitScore;
	}
	
	

}
