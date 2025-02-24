package com.stonksco.minitramways.logic.map.buildings;

import com.stonksco.minitramways.logic.map.Cell;

// Represents a shop in the map.
public class Shop extends Building {

	public Shop(Cell c) {
		super(c);
	}


	@Override
	public String toString() {
		return "Shop:"+GetCoordinates();
	}

}