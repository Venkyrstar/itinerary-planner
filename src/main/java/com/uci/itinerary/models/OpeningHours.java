package com.uci.itinerary.models;

import java.util.List;

public class OpeningHours {
	
	private boolean open_now;
	private List<String> weekday_text;

	public boolean getOpen_now() {
		return open_now;
	}

	public void setOpen_now(boolean open_now) {
		this.open_now = open_now;
	}

	public List<String> getWeekday_text() {
		return weekday_text;
	}

	public void setWeekday_text(List<String> weekday_text) {
		this.weekday_text = weekday_text;
	}

	@Override
	public String toString() {
		return "OpeningHours [open_now=" + open_now + ", weekday_text=" + weekday_text + "]";
	}
	
}
