package com.stonksco.minitramways.logic.map.lines;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.Station;

import java.util.*;

/**
 * Represents a line of tramway
 */
public class Line {

	/**
	 * Hash table which associates each station from the line with a multiple integer of 100 which corresponds to its position on the line
	 */
	private final HashMap<Integer,Station> Stations;
	private ArrayList<Tramway> Tramways;
	private LinePart First;

	public float GetSpeed() {
		return Speed;
	}

	private final float Speed = 3.5f;

	public int GetID() {
		return ID;
	}

	private final int ID;

	public Tramway AddTram() {
		if(Tramways==null)
			Tramways= new ArrayList<>();

		Tramway t = new Tramway(this);
		this.Tramways.add(t);
		return t;
	}

	/**
	 * Color code of the line
	 */
	private int Color;

	// Create a new line between two stations at the given positions
	public Line(Vector2 start, Vector2 end,int id) {
		First = new LinePart(this,start, end,null);
		this.ID = id;

		Stations = new HashMap<>();

		Station StartStation = Game.Get().GetMap().AddStation(start);

		Station EndStation = null;
		// If the second station already exists, then we do not create it and we add the line
		if(Game.Get().GetMap().GetCellAt(end).GetBuilding() instanceof Station) {
			EndStation = (Station)Game.Get().GetMap().GetCellAt(end).GetBuilding();
		} else {
			EndStation = Game.Get().GetMap().AddStation(end);
		}


		Stations.put(0,StartStation);
		Stations.put(100,EndStation);
		StartStation.AddLine(this);
		EndStation.AddLine(this);

		AddTram();
	}


	public LinePart GetFirstPart() {
		return First;
	}

	/**
	 * Divides a section in half according to an intermediate station
	 * @param at Place of the intermediate station
	 */
	public LinePart Divide(Vector2 start, Vector2 end ,Vector2 at) {
		return First.Divide(start,end,at);
	}

	public ArrayList<LinePart> GetParts() {
		ArrayList<LinePart> Res = new ArrayList<>();
		LinePart LP = First;
		while(LP != null) {
			Res.add(LP);
			LP = LP.GetNext();
		}
		return Res;
	}

	public LinePart Extend(Vector2 from, Vector2 to) {
		LinePart lp = null;
		Vector2 fromPos = null;
		Vector2 toPos = null;

		if(from.equals(First.GetStartPos())) {
			fromPos = to;
			toPos = from;
		} else if (from.equals(First.GetLast().GetEndPos())) {
			fromPos = from;
			toPos = to;
		}


		// If fromPos is one of the extremities of the line
		if(fromPos != null && toPos != null) {
			lp=new LinePart(this,fromPos,toPos,First);
			this.First.Add(lp);

			// We determine whether the station should be located at the start or at the end of the section
			Vector2 StationPos = null;
			if(from.equals(fromPos))
				StationPos = toPos.clone();
			else
				StationPos = fromPos.clone();

			Station NewStation = null;
			// If the second station already exists, then we do not create it and we add the line
			if(Game.Get().GetMap().GetCellAt(StationPos).GetBuilding() instanceof Station) {
				NewStation = (Station)Game.Get().GetMap().GetCellAt(StationPos).GetBuilding();
			} else {
				NewStation = Game.Get().GetMap().AddStation(StationPos);
			}

			if(lp.GetStartPos()==First.GetStartPos())
				Stations.put(lp.GetStart(),NewStation);
			else
				Stations.put(lp.GetEnd(),NewStation);

			NewStation.AddLine(this);
			((Station)Game.Get().GetMap().GetCellAt(from).GetBuilding()).AddLine(this);

			Game.Debug(1,"Line "+ID+" extended from "+from+" to "+to);
		}
		return lp;
	}

	public String toString() {
		String res = "-------EMPTY LINE------";
		if(First!=null)
			res= First.ToStringFull();
		return res;
	}

	public List<Map.Entry<Vector2,Vector2>> GetPartsVectors() {
		ArrayList<Map.Entry<Vector2,Vector2>> Res = new ArrayList<>();
		for(LinePart P : First.GetParts()) {
			Res.add(new AbstractMap.SimpleEntry<Vector2,Vector2>(P.GetStartPos(),P.GetEndPos()));
		}
		return Res;
	}

	public void SetFirst(LinePart newFirst) {
		this.First= newFirst;
	}


	public boolean IsAtExtremity(Vector2 pos) {
		boolean res = First.GetStartPos().equals(pos);
		if(First.GetLast().GetEndPos().equals(pos))
			res=true;
		return res;
	}

	public void Update() {
		for(Tramway T : Tramways) {
			// For each tram, we make it advance a certain distance
			T.Update();
		}
	}

	public Vector2 GetPositionAt(double at) {
		return First.GetPosAt(at);
	}


	public LinePart GetPartAt(double at) {
		LinePart Res = null;
		if(at>GetLastIndex())
			Res = First.GetPartAt(GetLastIndex());
		else if(at<GetFirstIndex())
			Res = First.GetPartAt(GetFirstIndex());
		else
			Res = First.GetPartAt(at);
		return Res;
	}

	public ArrayList<Tramway> GetTrams() {
		return (ArrayList<Tramway>) this.Tramways.clone();
	}

	public int GetFirstIndex() {
		return First.GetStart();
	}

	public int GetLastIndex() {
		return First.GetLast().GetEnd();
	}

	/**
	 * Removes a station from the line
	 */
    public boolean Trim(Vector2 Todestroy) {
		boolean Res = false;

		LinePart LP = null;
		if(First.GetStartPos().equals(Todestroy)) {
			LP=First;
		} else if(First.GetLast().GetEndPos().equals(Todestroy)){
			LP=First.GetLast();
		}

		if(LP!=null) {
			boolean NoTrams = true;
			for(Tramway T : GetTrams()) {
				if(T.GetLinePos()>=LP.GetStart() && T.GetLinePos()<=LP.GetEnd()) {
					NoTrams=false;
					break;
				}
			}

			if(NoTrams) {
				String OldLine = this.toString();
				LinePart NewExt = LP.Destroy(Todestroy);
				Res = true;
				if(First==LP)
					First=NewExt;
				Game.Debug(1,"Destroyed station "+Todestroy+" of part "+LP);
				Game.Debug(2,"       Old line :      "+OldLine);
				Game.Debug(2,"       Updated line :  "+this);
			}
		}

		return Res;
    }
}