package com.uci.itinerary.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DistanceMatrix {
	
	private List<Rows> rows;
	private String status;

	public List<Rows> getRows() {
		return rows;
	}

	public void setRows(List<Rows> rows) {
		this.rows = rows;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DistanceMatrix [rows=" + rows + ", status=" + status + "]";
	}
	
}
