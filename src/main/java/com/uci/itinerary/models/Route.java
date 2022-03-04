package com.uci.itinerary.models;

import java.util.ArrayList;

public class Route  implements Comparable<Route>{
	
	private ArrayList<TouristSpot> places;
	private double ratingScore;
	private double distanceScore;
	private double profitScore;

	public Route(ArrayList<TouristSpot> places, double ratingScore, double distanceScore, double profitScore) {
		super();
		this.places = places;
		this.ratingScore = ratingScore;
		this.distanceScore = distanceScore;
		this.profitScore = profitScore;
	}
	
	public Route(ArrayList<TouristSpot> places, double ratingScore, double distanceScore) {
		super();
		this.places = places;
		this.ratingScore = ratingScore;
		this.distanceScore = distanceScore;
	}

	public Route() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<TouristSpot> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<TouristSpot> places) {
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
	
	@Override
	public int compareTo(Route r) {
        return Double.compare(r.profitScore, this.profitScore);
    }
	

}
