package com.stonksco.minitramways.logic.people;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.Vector2Couple;
import com.stonksco.minitramways.logic.map.GameMap;
import com.stonksco.minitramways.logic.map.buildings.Station;

import java.util.*;

/**
 * Manage the pathfinding for people
 */
public class PathFinder {

    private GameMap GM;

    public PathFinder(GameMap gm) {
        this.GM = gm;
        UpdateGraph();
        StationsOfPath=new ArrayList<>();
    }

    
    private Vector2 From;
    private Vector2 To;

    private ArrayList<Vector2> Vertices;
    private HashMap<Vector2Couple,Integer> LineParts; 

    // Calculated or used data for calculations
    private HashMap<Vector2, Double> Distances;
    private HashMap<Vector2,Vector2> Parents;
    private ArrayList<Vector2> Open;
    private ArrayList<Vector2> Closed;
    private HashMap<Vector2,Double> FVals;
    private boolean FromHasStation = false; // A station serves the starting point
    private boolean ToHasStation = false; // A station serves the arrival point

    // Data produced by the pathfinding
    private ArrayList<Vector2> StationsOfPath; 


   
    private ArrayList<Vector2> GetAdjacentVertices(Vector2 of) {
        ArrayList<Vector2> res = new ArrayList<>();

        for(Vector2Couple part : LineParts.keySet()) {
            if(part.GetV1().equals(of))
                res.add(part.GetV2());
            else if(part.GetV2().equals(of))
                res.add(part.GetV1());
        }
        return res;
    }

    /**
     * Updates all the information on the graph (stations, lines) in the event of modifications
     */
    public void UpdateGraph() {
        
        Vertices = GM.GetStations();
        
        LineParts = new HashMap<>();
        for(int i = 0; i<GM.LinesCount(); i++) {
            for(Map.Entry<Vector2,Vector2> part : GM.GetPartsVectorsOf(i)) {
                LineParts.put(new Vector2Couple(part.getKey(),part.getValue()),i);
            }
        }
        // Create "false sections" to link the departure and arrival points to the stations that are accessible to them
        // If one of these steps fails, then the path will always be zero
        for(Vector2 s : Vertices) {
            if(Game.Get().GetMap().GetBuildingAt(s) instanceof Station) {
                if(From!=null)
                    if(Vector2.Distance(s,From)< ((Station)Game.Get().GetMap().GetBuildingAt(s)).Radius()) {
                        LineParts.put(new Vector2Couple(s,From),-1);
                        FromHasStation=true;
                    }
                if(To!=null)
                    if(Vector2.Distance(s,To)< ((Station)Game.Get().GetMap().GetBuildingAt(s)).Radius()) {
                        LineParts.put(new Vector2Couple(s,To),-1);
                        ToHasStation=true;
                    }
            }

        }

        Vertices.add(From);
        Vertices.add(To);

        StationsOfPath = new ArrayList<>(); // Reset path if we update the graph
    }

    public void ChangeTarget(Vector2 newTarget) {
        this.To = newTarget;
        StationsOfPath = new ArrayList<>(); // Reset path if we change a arrival point
        ToHasStation=false;
        UpdateGraph();
    }

    public void ChangeStart(Vector2 newStart) {
        this.From = newStart;
        StationsOfPath = new ArrayList<>(); // Reset path if we change a departure point
        FromHasStation=false;
        UpdateGraph();
    }



    /**
     * Get the path from the departure point to the arrival point
     * Returns the stations to go through with the shortest path
     */
    public ArrayList<Vector2> GetPath() {
        if(StationsOfPath.size()==0 && FromHasStation && ToHasStation) {
            ArrayList<Vector2> path = new ArrayList<>();

            Open=new ArrayList<>();
            Closed=new ArrayList<>();
            FVals = new HashMap<>();
            Parents = new HashMap<>();
            Distances = new HashMap<>();

            Vector2 current = From;
            double h = Vector2.AbstractDistance(current,To);
            FVals.put(current,h+0);
            Distances.put(current,0d);

            while(!current.equals(To)) {
                for(Vector2 a : GetAdjacentVertices(current)) {
                    if(!Open.contains(a) && !Closed.contains(a)) {
                        Open.add(a);
                        double g = Distances.get(current)+Vector2.AbstractDistance(current,a);
                        Distances.put(a,g);
                        h = Vector2.AbstractDistance(a,To);
                        double f = g+h;
                        Double currentfval = FVals.get(a);
                        if(currentfval==null || currentfval<f) {
                            FVals.put(a,f);
                            Parents.put(a,current);
                        }
                    }
                }
                Closed.add(current);
                Vector2 lowest = GetLowestFfromOpen();
                Open.remove(lowest);
                current=lowest;
                if(current==null)
                    break;
            }

            // we build the path by "going up the parents" from the arrival point to the departure point
            current = To;
            while(!current.equals(From)) {
                path.add(current);
                current=Parents.get(current);
                if(current==null) {
                    path.clear();
                    break;
                }
            }
            Collections.reverse(path);
            StationsOfPath=path;
            StationsOfPath.remove(From);
            StationsOfPath.remove(To);
        }
        return StationsOfPath;
    }

    private Vector2 GetLowestFfromOpen() {
        Vector2 res = null;
        double min = Double.POSITIVE_INFINITY;
        for(Vector2 v : Open) {
            if(FVals.get(v)<min) {
                res=v;
                min=FVals.get(v);
            }
        }
        return res;
    }


}
