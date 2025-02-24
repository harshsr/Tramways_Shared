package com.stonksco.minitramways.logic.map;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.Building;
import com.stonksco.minitramways.logic.map.buildings.House;
import com.stonksco.minitramways.logic.map.buildings.Office;
import com.stonksco.minitramways.logic.map.buildings.Shop;

import java.util.ArrayList;
import java.util.Random;
import java.util.SplittableRandom;

/**
 * Represented a district of a given type (residential, commercial or business)
 */
public class Area {

	ArrayList<Cell> Cells;
	ArrayList<Building> Buildings;
	/**
	 * Type of the area
	 */
	private final AreaTypes Type;

	// Density of the area (number of buildings)
	private final int Density = 3;


	public Area(ArrayList<Cell> area,AreaTypes areasType) {
		Buildings = new ArrayList<>();
		this.Cells = area;
		this.Type = areasType;
	}

	/**
	 * Add a building to the area
	 */
	private void addBuilding(Cell at) {
		switch (Type){
			case office:
				Office o = new Office(at);
				at.SetBuilding(o);
				Buildings.add(o);
				break;
			case shopping:
				Shop s = new Shop(at);
				at.SetBuilding(s);
				Buildings.add(s);
				break;
			case residential:
				House h = new House(at);
				at.SetBuilding(h);
				Buildings.add(h);
				break;
		}

	}

	/**
	 * Return the density of the area
	 */
	public double GetDensity() {
		int nbOfCells = Cells.size();
		int notEmptyCells = Buildings.size();
		double res = (double)notEmptyCells/(double)nbOfCells;
		return res;
	}

	private SplittableRandom randGen = new SplittableRandom();

	private Cell GetRandomCell() {
		int rand = Math.round(randGen.nextInt(Cells.size()-1));
		if(rand<0)
			rand=0;
		return Cells.get(rand);
	}

	/**
	 *  Request to generate a building in the area
	 */
	public boolean GenerateBuilding() {
		boolean res=false;

		Cell c = null;

		int toGenerate = 1;
		if(Type==AreaTypes.residential)
			toGenerate = 2+(int)(randGen.nextInt(3));

		for(int i = 0; i<500; i++) {
			c = GetRandomCell();
			if(c!=null) {
				addBuilding(c);
				toGenerate--;
				res=true;
			}

			if(toGenerate==0)
				break;
		}

		return res;

	}

    public boolean IsIn(Vector2 pos) {
		return Cells.contains(Game.Get().GetMap().GetCellAt(pos));
    }

	public AreaTypes GetType() {
		return Type;
	}

	public ArrayList<Building> GetBuildings() {
		return Buildings;
	}

	public ArrayList<Cell> GetCells() {
		return Cells;
	}

	public Building GetRandomBuilding() {
		Building res = null;

		int nb = randGen.nextInt(Buildings.size());
		res = Buildings.get(nb);
		return res;
	}


}