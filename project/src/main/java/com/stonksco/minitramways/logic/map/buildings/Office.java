package com.stonksco.minitramways.logic.map.buildings;

import com.stonksco.minitramways.logic.map.Cell;

// Represents an office in the map.
public class Office extends Building {

	
	public Office(Cell c) {
		super(c);
	}


	@Override
	public String toString() {
		return "Office:"+GetCoordinates();
	}

}