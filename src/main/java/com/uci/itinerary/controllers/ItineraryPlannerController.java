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
import com.uci.itinerary.models.DestinationPlaceDetails;
import com.uci.itinerary.models.DistanceMatrix;
import com.uci.itinerary.models.Elements;
import com.uci.itinerary.models.Metrics;
import com.uci.itinerary.models.Place;
import com.uci.itinerary.models.PlaceDetail;
import com.uci.itinerary.models.PlaceSearch;
import com.uci.itinerary.models.Rows;


@RestController
@RequestMapping("/itinerary-planner")
public class ItineraryPlannerController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("/api/plan")
	public DestinationPlaceDetails getItineraryPlan(@RequestParam String placeName) {
		
		String pageToken = null;
		DestinationPlaceDetails dpd = new DestinationPlaceDetails();
		List<Place> places = new LinkedList<>();
		
		try {
	
			do {
				PlaceSearch placeSearch;				
				if(pageToken == null) {
					placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query={name}&key={key}", PlaceSearch.class, "tourist spots in " + placeName , "API_KEY");
				}
				else {
					placeSearch = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?pagetoken=" + pageToken + "&key=API_KEY", PlaceSearch.class);			
				}
				places.addAll(placeSearch.getResults());
				pageToken = placeSearch.getNextPageToken();
				
			}while(pageToken != null);
			
			Iterator<Place> iterator = places.iterator();
			
			while(iterator.hasNext()) {
				Place place = iterator.next();
				if(place.getPermanently_closed() == true || place.getOpening_hours() == null)
					iterator.remove();
			}
			
			Collections.sort(places, Collections.reverseOrder());
			
			places = places.subList(0, Math.min(10, places.size()));
			
			for (Place place : places) {
				if (place.getPlace_id() != null) {
					PlaceDetail placeDetail;
					placeDetail = restTemplate.getForObject("https://maps.googleapis.com/maps/api/place/details/json?fields=opening_hours" + "&place_id=" + place.getPlace_id() + "&key=API_KEY", PlaceDetail.class);
					place.setOpening_hours(placeDetail.getResult().getOpening_hours());
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
			distanceMatrix = restTemplate.getForObject("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + distanceParam + "&destinations=" + distanceParam + "&key=API_KEY", DistanceMatrix.class);
			
			List<List<Metrics>> matrix = new ArrayList<>();
			
			for(Rows rows : distanceMatrix.getRows()) {
				List<Metrics> metrics = new ArrayList<>();
				for(Elements element : rows.getElements()) {
					Metrics metric = new Metrics(element.getDistance().getValue(), element.getDuration().getValue());
					metrics.add(metric);
				}
				matrix.add(metrics);
			}
			for(int i = 0; i < matrix.size(); i++) {
				System.out.println("row " + i + " ");
				for(int j = 0; j < matrix.get(i).size(); j++) {
					System.out.print(matrix.get(i).get(j).getDistance() + ", " + matrix.get(i).get(j).getDistance() + " ");
				}
				System.out.println();
				System.out.println();
			}
			dpd.setMatrix(matrix);
			dpd.setStatus(Constants.OK);
		
		}catch (Exception e) {
			dpd.setStatus(Constants.ERROR);
			dpd.setErrorMessage(e.getMessage());
		}
		
		return dpd;
		
	}
}
