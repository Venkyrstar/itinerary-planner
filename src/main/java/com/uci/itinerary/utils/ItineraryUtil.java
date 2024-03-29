package com.uci.itinerary.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.uci.itinerary.models.Itinerary;
import com.uci.itinerary.models.Place;
import com.uci.itinerary.models.Route;

public class ItineraryUtil {
//	public ArrayList<Itinerary> itineraryAlgo(ArrayList<Place> places, Date fromDate, Date toDate) {
//		Collections.sort(places, Collections.reverseOrder()); // based on rating
//
//		// calculating noOfDays
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
//		long diff = toDate.getTime() - fromDate.getTime();
//		TimeUnit time = TimeUnit.DAYS;
//		long noOfDays = time.convert(diff, TimeUnit.MILLISECONDS);
//
//		// ArrayList<Place> placesList = (ArrayList<Place>) places.subList(0, (int)
//		// noOfDays*3); // Need to consider more places than the number of places in
//		// final itinerary
//
//		// Calculate routes
//		ArrayList<ArrayList<Integer>> permuteRoutes = new ArrayList<ArrayList<Integer>>();
//		routePermute(permuteRoutes, new ArrayList<>(), places.size(), 3);
//
//		ArrayList<Route> routes = new ArrayList<Route>();
//
//		for (int i = 0; i < permuteRoutes.size(); ++i) {
//			Route r = new Route();
//			ArrayList<Place> p = new ArrayList<Place>();
//			for (int j = 0; j < permuteRoutes.get(i).size(); ++j) {
//				p.add(places.get(permuteRoutes.get(i).get(j)));
//			}
//			r.setPlaces(p);
//			routes.add(r);
//		}
//
//		for (Route r : routes) {
//			r.setRatingScore(calculateRatingScore(r));
//			r.setDistanceScore(calculateDistanceScore(r));
//			r.setProfitScore(calculateProfitScore(r));
//		}
//		Collections.sort(routes, Collections.reverseOrder()); // based on profit score
//
//		// Prepare Itinerary
//		ArrayList<Itinerary> itinerary = prepareItinerary(routes, toDate, fromDate, noOfDays);
//		return itinerary;
//	}

//	public ArrayList<Itinerary> prepareItinerary(ArrayList<Route> routes, Date toDate, Date fromDate, long noOfDays) {
//		HashMap<Place, Boolean> visited = new HashMap<>();
//		Date currDate = fromDate;
//		ArrayList<Itinerary> itinerary = new ArrayList<Itinerary>();
//		for (Route r : routes) {
//			boolean isValid = true;
//			for (Place p : r.getPlaces()) {
//				if (visited.containsKey(p)) {
//					isValid = false;
//					break;
//				} else {
//					visited.put(p, true);
//				}
//			}
//			if (isValid) {
//				itinerary.add(new Itinerary(currDate, r));
//				Calendar c = Calendar.getInstance();
//				c.setTime(currDate);
//				c.add(Calendar.DATE, 1);
//				currDate = c.getTime();
//				noOfDays--;
//			}
//			if (noOfDays == 0) {
//				break;
//			}
//		}
//		return itinerary;
//	}

	

//	public double calculateRatingScore(Route r) {
//		return 0;
//
//	}
//
//	public double calculateDistanceScore(Route r) {
//		return 0;
//
//	}
//
//	public double calculateProfitScore(Route r) {
//		return 0;
//
//	}
}
