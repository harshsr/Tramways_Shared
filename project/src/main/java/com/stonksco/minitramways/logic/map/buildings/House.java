package com.stonksco.minitramways.logic.map.buildings;

import com.stonksco.minitramways.logic.people.People;
import com.stonksco.minitramways.logic.map.Cell;

// Represents a house in the map.
public class House extends Building {

	
	public House(Cell c) {
		super(c);
	}

	// Adds a person to the house
	public People Spawn() {
		People P = new People(this);
		this.People.add(P);
		return P;
	}

	@Override
	public String toString() {
		return "House:"+GetCoordinates();
	}
}