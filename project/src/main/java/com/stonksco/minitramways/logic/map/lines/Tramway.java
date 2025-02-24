package com.stonksco.minitramways.logic.map.lines;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.people.People;
import com.stonksco.minitramways.logic.map.PlaceToBe;
import com.stonksco.minitramways.logic.map.buildings.Station;
import com.stonksco.minitramways.views.Clock;

import java.util.ArrayList;

public class Tramway implements PlaceToBe {

	Line Line;
	ArrayList<People> People;

	/**
	 * Visited station before running station
	 * This data helps to know in which direction the tram is heading
	 */
	private Station LastVisitedStation;

	public double GetLinePos() {
		return LinePos;
	}

	/**
	 * Position on the line, with percentage slices
	 * For example, if the tram is halfway between the second and the third station of the line, then this given is 250
	 */
	private double LinePos;
	private LinePart CurrentPart;
	private Vector2 RealPos;

	public int GetCapacity() {
		return Capacity;
	}

	/**
	 * Maximum number of people that this tram can accommodate
	 */
	private int Capacity = 8;



	/**
	 * 
	 * @param Line
	 */
	public Tramway(Line l) {
		this.Line = l;
		LinePos = 0;
		People = new ArrayList<>();
	}

	/**
	 * Return the number of people currently in this tram
	 */
	public int Amount() {
		return People.size();
	}

	/**
	 * Move a person in the current tram
	 * @param p
	 */
	public void Enter(People p) {
		People.add(p);
		p.GetCurrentPlace().Exit(p);
		p.SetCurrentPlace(this);
	}

	/**
	 * Remove a person from the current tram
	 * @param p
	 */
	public void Exit(People p) {
		People.remove(p);
	}

	@Override
	public Vector2 GetCoordinates() {
		return RealPos.clone();
	}

	// Moving direction: True = front, false = rear
	private boolean MoveDirection = true;

	private long TimeSinceLastUpdateNs = 0;

	public void Update() {

		if(TimeSinceLastUpdateNs > 20000000) {

			if(CurrentPart==null)
				CurrentPart=Line.GetPartAt(LinePos);
			// Tram displacement at a constant speed
			double increment = 1/ CurrentPart.GetLength()*Line.GetSpeed()*(TimeSinceLastUpdateNs/Math.pow(10,9))*100;
			if(!MoveDirection)
				increment *=-1;

			double newpos = LinePos + increment;

			if(newpos<Line.GetFirstIndex()) {
				newpos = Line.GetFirstIndex()+(Line.GetFirstIndex() - newpos);
				MoveDirection = !MoveDirection;
			} else if(newpos>=Line.GetLastIndex()) {
				newpos = Line.GetLastIndex()-(newpos-Line.GetLastIndex());
				MoveDirection = !MoveDirection;
			}

			LinePart newPart = Line.GetPartAt(newpos);
			if(newPart!=null) {
				if (!newPart.equals(CurrentPart))
					Game.Debug(2, "Tramway changed of part from " + CurrentPart + " to " + newPart + " on line " + Line.GetID());

				Game.Debug(4, "Tramway moved from " + LinePos + " to " + newpos + " on line " + Line.GetID());

				LinePos = newpos;
				CurrentPart = newPart;
				TimeSinceLastUpdateNs = 0;

				UpdateRealPos();
			}
		} else {
			TimeSinceLastUpdateNs += Clock.Get().GameDeltaTimeNs();
		}

		
	}

	public void PositionAt(double linePos) {
		this.LinePos = linePos;
		CurrentPart = Line.GetPartAt(linePos);
		UpdateRealPos();
	}

	private void UpdateRealPos() {
		Vector2 scaledPartPos = CurrentPart.GetEndPos().Sub(CurrentPart.GetStartPos()).Scale((LinePos%100)/100d);
		Vector2 pos = this.CurrentPart.GetStartPos().Add(scaledPartPos);
		RealPos = pos;
	}

	public int LineID() {
		return Line.GetID();
	}

	public Vector2 GetTarget() {
		Vector2 res = null;
		if(MoveDirection)
			res = CurrentPart.GetEndPos();
		else
			res = CurrentPart.GetStartPos();
		return res;
	}

	public LinePart GetCurrentPart() {
		return CurrentPart;
	}

}