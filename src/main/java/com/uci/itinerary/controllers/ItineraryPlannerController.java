package com.uci.itinerary.controllers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.uci.itinerary.utils.Constants;
import com.uci.itinerary.utils.ItineraryUtil;
import com.uci.itinerary.models.DestinationPlaceDetails;
import com.uci.itinerary.models.DistanceMatrix;
import com.uci.itinerary.models.Elements;
import com.uci.itinerary.models.Interval;
import com.uci.itinerary.models.Itinerary;
import com.uci.itinerary.models.Metrics;
import com.uci.itinerary.models.Period;
import com.uci.itinerary.models.Place;
import com.uci.itinerary.models.PlaceDetail;
import com.uci.itinerary.models.PlaceSearch;
import com.uci.itinerary.models.Route;
import com.uci.itinerary.models.Rows;
import com.uci.itinerary.models.TouristSpot;
import com.uci.itinerary.models.TravelTime;


@RestController
@RequestMapping("/itinerary-planner")
public class ItineraryPlannerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("/api/plan")
	public Itinerary getItineraryPlan(@RequestParam String placeName) {
		
		String pageToken = null;
		DestinationPlaceDetails dpd = new DestinationPlaceDetails();
		List<Place> places = new LinkedList<>();
		Itinerary itinerary = new Itinerary();
		
		try {
	
			do {
				PlaceSearch placeSearch;				
				if(pageToken == null) {
					placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query={name}&key={key}", PlaceSearch.class, "tourist spots in " + placeName , Constants.API_KEY);
				}
				else {
					placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken=" + pageToken + "&key=" + Constants.API_KEY, PlaceSearch.class);			
				}
				for(Place place : placeSearch.getResults()) {
					if(place.getPermanently_closed() != true && place.getPlace_id() != null)
						places.add(place);
				}
				pageToken = placeSearch.getNextPageToken();
				
			}while(pageToken != null);
			
			
			Collections.sort(places, Collections.reverseOrder());
			
			places = places.subList(0, Math.min(10, places.size()));
			
			PlaceDetail placeDetail;	
			List<List<Interval>> timings;
			Interval interval; int day;
			
			Iterator<Place> iterator = places.iterator();
			
			while(iterator.hasNext()) {
				Place place = iterator.next();
				placeDetail = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/details/json?fields=opening_hours" + "&place_id=" + place.getPlace_id() + "&key=" + Constants.API_KEY, PlaceDetail.class);
				if(placeDetail.getResult().getOpening_hours() != null) {
					timings = new ArrayList<>(Collections.nCopies(7, null));
					for(Period period : placeDetail.getResult().getOpening_hours().getPeriods()) {
						
						day = period.getOpen().getDay();
						if(period.getClose() == null) 
							interval = new Interval(period.getOpen().getTime(), 0000);
						else
							interval = new Interval(period.getOpen().getTime(), period.getClose().getTime());
						List<Interval> intervals = timings.get(day);
						if(intervals == null)
							intervals = new ArrayList<Interval>();
						intervals.add(interval);
						timings.set(day, intervals);
					}
					place.setTimings(timings);
				}else {
					iterator.remove();
				}
			}
			
			dpd.setPlaces(places);
			
			String distanceParam = new String();
			for (int i = 0; i < places.size(); i++) {
				if (i == places.size()-1) {
					distanceParam =  distanceParam + "place_id:" + places.get(i).getPlace_id();
					break;
				} else {
					distanceParam =  distanceParam + "place_id:" + places.get(i).getPlace_id() + "|";
				}
			}
			
			DistanceMatrix distanceMatrix;
			distanceMatrix = restTemplate.getForObject("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + distanceParam + "&destinations=" + distanceParam + "&key=" + Constants.API_KEY, DistanceMatrix.class);
			
			List<List<Metrics>> matrix = new ArrayList<>();
			
			for(Rows rows : distanceMatrix.getRows()) {
				List<Metrics> metrics = new ArrayList<>();
				for(Elements element : rows.getElements()) {
					Metrics metric = new Metrics(element.getDistance().getValue(), element.getDuration().getValue());
					metrics.add(metric);
				}
				matrix.add(metrics);
			}
			dpd.setMatrix(matrix);
			ArrayList<ArrayList<Integer>> routePermuations = new ArrayList<>(); 
			routePermute(routePermuations, new ArrayList<>(), places.size(), 4);
			
			day = 0;
			
			
			List<Interval> placeTimings;
			//Iterator<ArrayList<Integer>> it = routePermuations.iterator();
			int start, open, close, i, j, oneDay = 24 * 60;
			double ratingScore = 0, distanceScore = 0, maxRatingScore = -1, maxDistanceScore = -1;
			Place place;
			List<Route> routes = new ArrayList<>();
			
			int cnt = 0; // debugging
			for(ArrayList<Integer> route : routePermuations) {
				ratingScore = 0; distanceScore = 0;
				ArrayList<TravelTime> travelTimes = new ArrayList<>();
				place = places.get(route.get(0));
				placeTimings = place.getTimings().get(day); // first place among 3
				if(placeTimings == null) {
					cnt++;
					continue; // invalid if place has no open timings on given day of the week
				}
				start = getMinutes(Math.max(900, placeTimings.get(0).getOpen())); // earliest opening hours;
				travelTimes.add(new TravelTime(getHours(start), getHours((start + 120) % oneDay)));
				start = (start + 120) % oneDay;
				
				ratingScore += (place.getRating() * place.getUser_ratings_total());
				
				for(i = 1; i < route.size(); i++) {
					start  = (start + (int) (matrix.get(route.get(i - 1)).get(route.get(i)).getDuration() / 60)) % oneDay; // travel to next place
					place = places.get(route.get(i));
					ratingScore += (place.getRating() * place.getUser_ratings_total());
					
					distanceScore += matrix.get(route.get(i - 1)).get(route.get(i)).getDistance();
					
					placeTimings = place.getTimings().get(day);
					if(placeTimings == null) {
						break; // invalid if place has no open timings on given day of the week
					}
					for(j = 0; j < placeTimings.size(); j++) {
						interval = placeTimings.get(j);
						open  = getMinutes(interval.getOpen());
						close = getMinutes(interval.getClose());
						if(start >= open && (start + 120) % oneDay <= close) {
							travelTimes.add(new TravelTime(getHours(start), getHours((start + 120) % oneDay)));
							break;
						}
							
					}
					if(j == placeTimings.size()) {
						break; // invalid if place is not open at required time
					}
					start = (start + 120) % oneDay;
				}
				
				if(i == route.size()) {
					ratingScore /= route.size();
					maxRatingScore = Math.max(maxRatingScore, ratingScore);
					maxDistanceScore = Math.max(maxDistanceScore, distanceScore);
					ArrayList<TouristSpot> p = new ArrayList<>(); Place temp;
					for(int s  = 0; s < route.size(); s++) {
						temp = places.get(route.get(s));
						p.add(new TouristSpot(travelTimes.get(s).getStart(), travelTimes.get(s).getEnd(), temp.getName(), temp.getFormatted_address(), temp.getRating(), temp.getUser_ratings_total(), temp.getTypes()));
					}
					routes.add(new Route(p, ratingScore, distanceScore));
				}
			}
			double profitScore;
			for(Route route : routes) {
				profitScore = 2 * (route.getRatingScore()/ maxRatingScore) - (route.getDistanceScore() / maxDistanceScore);
				route.setProfitScore(profitScore);
			}
			Collections.sort(routes);
			
			itinerary.setRoute(routes.get(0));
			
			itinerary.setStatus(Constants.OK);
		}catch (Exception e) {
			itinerary.setStatus(Constants.ERROR);
			itinerary.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return itinerary;
		
	}
	
	private void routePermute(ArrayList<ArrayList<Integer>> list, ArrayList<Integer> tempList, int placesCount,
			int permutationSize) {
		if (tempList.size() == permutationSize) {
			list.add(new ArrayList<>(tempList));
			// System.out.println(list.get(list.size() - 1));
		} else {
			for (int i = 0; i < placesCount; ++i) {
				if (tempList.contains(i))
					continue;
				tempList.add(i);
				routePermute(list, tempList, placesCount, permutationSize);
				tempList.remove(tempList.size() - 1);
			}
		}

	}
	
	private String getHours(int time) {
		String s =  String.valueOf(time / 60) + ":";
		if(time % 60 < 10) s = s + "0" + String.valueOf(time % 60);
		else s = s + String.valueOf(time % 60);
		return s;
			
	}

	int getMinutes(int time) {
		return ((time / 100) * (60) + (time % 100)) % (24 * 60);
	}
}
