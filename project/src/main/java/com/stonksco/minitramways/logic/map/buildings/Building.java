package com.stonksco.minitramways.logic.map.buildings;

import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.people.People;
import com.stonksco.minitramways.logic.map.Cell;
import com.stonksco.minitramways.logic.map.PlaceToBe;

import java.util.ArrayList;
import java.util.Objects;

// Represents a building
public abstract class Building implements PlaceToBe {

	Cell Cell;
	protected ArrayList<People> People;

	// Return the box corresponding to the building
	public Cell GetCell() {
		return this.Cell;
	}

	public Building(Cell c) {
		this.Cell = c;
		c.SetBuilding(this);
		People = new ArrayList<>();
	}

	@Override
	public int Amount() {
		return People.size();
	}

	@Override
	public void Enter(People p){
		People.add(p);
		p.GetCurrentPlace().Exit(p);
		p.SetCurrentPlace(this);
	}

	@Override
	public void Exit(People p) {
		this.People.remove(p);
	}

	@Override
	public Vector2 GetCoordinates() {
		return this.Cell.GetCoordinates();
	}

	@Override
	public String toString() {
		return "Building:"+GetCoordinates();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Building building = (Building) o;
		return Cell.equals(building.Cell) && People.equals(building.People);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Cell, People);
	}
}