package com.stonksco.minitramways.logic.map;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.Building;

import java.util.Objects;

/**
 * Represents a cell of the map grid
 */
public class Cell {

	private final GameMap Map;

	private Area Area;
	private Building Building;
	private final Vector2 Coordinates;


	// Get the building occupying the cell
	public Building GetBuilding() {
		return Building;
	}

	// Get the area of the cell
	public Area GetArea() {
		return this.Area;
	}

	// Set the building occupying the cell
	public void SetBuilding(Building b) {
		this.Building = b;
	}

	// Set the area of the cell
	public void SetArea(Area a) {
		this.Area = a;
	}


	public Cell(Vector2 pos) {
		Map= Game.Get().GetMap();
		this.Coordinates = pos;
	}

	public Vector2 GetCoordinates() {
		return Coordinates.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cell cell = (Cell) o;
		return Coordinates.equals(cell.Coordinates);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Coordinates);
	}
}