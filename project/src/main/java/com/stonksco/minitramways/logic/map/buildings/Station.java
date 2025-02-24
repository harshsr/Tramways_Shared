package com.stonksco.minitramways.logic.map.buildings;

import com.stonksco.minitramways.logic.people.People;
import com.stonksco.minitramways.logic.map.Cell;
import com.stonksco.minitramways.logic.map.lines.Line;

import java.util.ArrayList;
import java.util.HashMap;

// Represents a station in the map.
public class Station extends Building {

	// Lines that serve the station
	HashMap<Integer,Line> Lines;

	
	// People in the station
	private ArrayList<People> People;

	public int GetCapacity() {
		return Capacity;
	}

	// Maximum number of people in the station
	private int Capacity;

	// Radius in which the station can serve buildings
	private final double Radius;

	// Constructor
	public Station(Cell c) {
		super(c);
		People = new ArrayList<>();
		Capacity = 14;
		Radius = 4;
	}

	// Adds a line to the station
	public boolean AddLine(Line line) {
		if(Lines==null)
			Lines = new HashMap<>();
		return !(Lines.put(line.GetID(),line)==null);
	}

	// Return all the lines that serve the station
	public Line[] GetLines() {
		Line[] Res;
		if(Lines.size()>0)
			Res = Lines.values().toArray(new Line[0]);
		else
			Res = null;
		return Res;
	}


	@Override
	public String toString() {
		return "Station:"+GetCoordinates();
	}

	public double Radius() {
		return Radius;
	}

    public void RemoveLine(int id) {
		
    }
}