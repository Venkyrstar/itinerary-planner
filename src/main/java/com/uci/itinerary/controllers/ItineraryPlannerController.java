package com.uci.itinerary.controllers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.uci.itinerary.models.PlaceSearch;
import com.uci.itinerary.models.Place;
import com.uci.itinerary.models.PlaceDetail;

@RestController
@RequestMapping("/itinerary-planner")
public class ItineraryPlannerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("/api/plan")
	public List<Place> getItineraryPlan(@RequestParam String placeName) {
		
		String pageToken = null;
		List<Place> places = new LinkedList<>();
		
		
		do {
			//System.out.println(pageToken);
			
			PlaceSearch placeSearch;
			
			if(pageToken == null) {
				placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query={name}&key={key}", PlaceSearch.class, "tourist spots in " + placeName , "Your_API_Key");
			}
			else {
				placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken=" + pageToken + "&key=Your_API_Key", PlaceSearch.class);			
			}
			places.addAll(placeSearch.getResults());
			
			pageToken = placeSearch.getNextPageToken();
			
		}while(pageToken != null);
		
		
		Iterator<Place> iterator = places.iterator();
		
		while(iterator.hasNext()) {
			Place place = iterator.next();
			if(place.getPermanently_closed() == true)
				iterator.remove();
		}
		
		for (Place place : places) {
			if (place.getPlace_id() != null) {
				PlaceDetail placeDetail;
				placeDetail = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/details/json?fields=opening_hours" + "&place_id=" + place.getPlace_id() + "&key=Your_API_Key", PlaceDetail.class);
				place.setOpening_hours(placeDetail.getResult().getOpening_hours());
			}
		}
		
		return places;
	}
}
