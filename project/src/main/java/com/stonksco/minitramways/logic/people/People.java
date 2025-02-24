package com.stonksco.minitramways.logic.people;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.Cell;
import com.stonksco.minitramways.logic.map.PlaceToBe;
import com.stonksco.minitramways.logic.map.buildings.Building;
import com.stonksco.minitramways.logic.map.buildings.Station;
import com.stonksco.minitramways.logic.map.lines.LinePart;
import com.stonksco.minitramways.logic.map.lines.Tramway;
import com.stonksco.minitramways.views.Clock;

import java.util.ArrayList;
import java.util.Collections;

public class People {

	private static ArrayList<People> Instances = new ArrayList<>();

	private static final PathFinder PathFinder = new PathFinder(Game.Get().GetMap());

	public static ArrayList<People> GetAll() {
		return (ArrayList<People>)Instances.clone();
	}

	Vector2 SpawnedAt;
	PlaceToBe Place;
	PlaceToBe Target;
	ArrayList<PlaceToBe> PathToFollow;
	/**
	 * Time in seconds from which the person is waiting for (is not increasing if the person has no target, and is reset to 0 as soon as the person moves)
	 */
	private double WaitingSince;

	private int Satisfaction = 0;

	private boolean PathNeedsUpdate = true;


	public People(PlaceToBe place) {
		Instances.add(this);
		this.Place = place;
		SpawnedAt = place.GetCoordinates();
		PathToFollow = new ArrayList<>();

		SetRandTarget();
	}

	private void GetPath() {
		if(Game.Get().GetMap().GetStations().size()>1) {
			if(Place instanceof Tramway) {
				PathFinder.ChangeStart(((Tramway)Place).GetTarget());
			} else {
				PathFinder.ChangeStart(Place.GetCoordinates());
			}
			PathFinder.ChangeTarget(Target.GetCoordinates());
			ArrayList<Vector2> foundPath = PathFinder.GetPath();
			if(foundPath.size()>0) {
				for(Vector2 v : foundPath) {
					PathToFollow.add(Game.Get().GetMap().VectorToPlace(v));
				}
				Game.Debug(2,"Chemin trouvé pour "+this+" :  -   Chemin = "+PathToFollow);
				PathNeedsUpdate=false;
			}
		}
	}

	/**
	 * Return the time in seconds from which the person is waiting for
	 */
	public double GetWait() {
		return WaitingSince;
	}

	/**
	 * Reset the waiting time
	 */
	public void ResetWait() {
		WaitingSince=0;
	}


	public void SetRandTarget() {
		this.Target = Game.Get().GetMap().getRandomTarget();
	}

	public Vector2 GetTargetPos() {
		Vector2 res = null;
		if(this.Target!=null)
			res = Target.GetCoordinates();
		return res;
	}

	public void Update() {
		if((PathToFollow.size() == 0 && Target!=null) || PathNeedsUpdate) {
			GetPath();
		} else {
			ChooseMove();
		}

	}

	public PlaceToBe GetCurrentPlace() {
		return Place;
	}

	public void SetCurrentPlace(PlaceToBe place) {
		this.Place = place;
	}
	/**
	 * Updates the pathfinder graph to take into account the latest line changes
	 */
	public static void UpdateGraph() {
		PathFinder.UpdateGraph();
	}

	/**
	 * Method that determines whether a displacement must be made and, if necessary, which one
	 */

	private void ChooseMove() {
		if(PathToFollow.size()>0) {
			//System.out.println("TEMP searching for move from "+place.getCoordinates()+" to "+pathToFollow.get(0).getCoordinates());
			if(Place instanceof Tramway) {
				// If we are currently in a tram (<=> we are not at the starting point)
				Vector2 targetPos = PathToFollow.get(0).GetCoordinates();
				// We check if we are "on" the targeted station
				if(Vector2.AbstractDistance(targetPos,GetCurrentPlace().GetCoordinates())<0.2d) {

					if(PathToFollow.size()>1) {
						// If we are not on the arrival station
						// If the tram continues on the right line, then we stay in it
						if(Game.Get().GetMap().IsOnLine(PathToFollow.get(1).GetCoordinates(), ((Tramway) Place).LineID())) {
							PathToFollow.remove(0); // We consider that the station is visited
						} else {
							// Otherwise, we go down to the station
							Move(PathToFollow.get(0));
						}
					} else {
						// If we are on the finish station
						Move(Target);
					}


				}
			} else if(Place instanceof Station){
				// Waiting for a tram

					// Increment the waiting time
					WaitingSince+= Clock.Get().GameDeltaTime();


					LinePart part = Game.Get().GetMap().GetPartBetween(Place.GetCoordinates(),PathToFollow.get(0).GetCoordinates());

					if(part!=null) {
						Tramway closestTram = null;
						double closestTramDistance = Double.POSITIVE_INFINITY;


						for(Tramway t : part.GetLine().GetTrams()) {
							double tramDistance = Vector2.AbstractDistance(t.GetCoordinates(),Place.GetCoordinates());
							if(tramDistance<closestTramDistance) {
								closestTramDistance = tramDistance;
								closestTram = t;
							}
						}

						//System.out.println("TEMP closest tram is at "+closestTramDistance);

						// If one of the lines on the line is "on" the station and if he goes to the target
						if(closestTramDistance<0.15d && closestTram.GetTarget().equals(PathToFollow.get(0).GetCoordinates()))
							if(closestTram.Amount()<closestTram.GetCapacity())
								Move(closestTram);
					} else {
						//System.out.println("TEMP : PART NULL");
					}

			} else {
				// We are at the starting point
			
				// If the target station has room
				if(((Station)PathToFollow.get(0)).GetCapacity() > PathToFollow.get(0).Amount()) {
					Move(PathToFollow.get(0));
				}
					
			}
		}
	}

	/**
	 * Method that performs a movement towards the next building

	 */
	private void Move(PlaceToBe to) {


		to.Enter(this);
		if(to.equals(PathToFollow.get(0)))
			PathToFollow.remove(0);

		if(Place.equals(Target)) {
			PathToFollow.clear();
			Target=null;
			Dispawn();
		}

		Game.Get().RequestPinsUpdate();
	}

	private void Dispawn() {
		ComputeSatisfaction();
		AddMoney();

		People.Instances.remove(this);
		this.Place=null;
		this.Target=null;
		this.PathToFollow=null;
	}

	
    public int ComputeSatisfaction() {
		int val = (int) (100-(WaitingSince/Vector2.Distance(Place.GetCoordinates(),SpawnedAt)*10d));
		if(val<0)
			val=0;
		Game.Get().SendSatisfaction(this,val);
		return val;
    }

    private void AddMoney() {
		Game.Get().AddMoney(5);
    }

	
	public void AddIntersectionBetween(Vector2 intersection, Vector2 v1, Vector2 v2) {

		if(PathToFollow.size()>0){
			if(Place instanceof Tramway) {
		
				LinePart currentTramPart = ((Tramway)Place).GetCurrentPart();
	
				if((currentTramPart.GetStartPos().equals(v1) && currentTramPart.GetEndPos().equals(v2)) || (currentTramPart.GetStartPos().equals(v2) && currentTramPart.GetEndPos().equals(v1))) {

					PathToFollow.add(0,Game.Get().GetMap().GetBuildingAt(intersection));
				}

			} else {

				if(PathToFollow.size()==1) {
		
					PathToFollow.add(0,Game.Get().GetMap().GetBuildingAt(intersection)); 
				} else {
	

					if(PathToFollow.get(0).GetCoordinates().equals(v1) || PathToFollow.get(0).GetCoordinates().equals(v2)) {

						PathToFollow.add(0,Game.Get().GetMap().GetBuildingAt(intersection));
					} else {
	
						for(int i = 0; i<PathToFollow.size()-1; i++) {
							Vector2 p1 = PathToFollow.get(i).GetCoordinates();
							Vector2 p2 = PathToFollow.get(i+1).GetCoordinates();
							if((p1.equals(v1) && p2.equals(v2)) || (p1.equals(v2) && p2.equals(v1))) {
								PathToFollow.add(i+1,Game.Get().GetMap().GetBuildingAt(intersection));
								break;
							}

							if((p1.equals(Place.GetCoordinates()) && p2.equals(PathToFollow.get(0))) || (p2.equals(Place.GetCoordinates()) && p1.equals(PathToFollow.get(0)))) {
								PathToFollow.add(0,Game.Get().GetMap().GetBuildingAt(intersection));
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "People:["+Place.toString()+"]";
	}

    public void destroyed(Vector2 stationtodestroy) {
		boolean pathContainsDestroyed = false;
		for(PlaceToBe p : PathToFollow) {
			if(p.GetCoordinates().equals(stationtodestroy)) {
				pathContainsDestroyed = true;
				break;
			}
		}

		if(pathContainsDestroyed) {
			PathToFollow.clear();
			PathNeedsUpdate = true;
		}

    }
}