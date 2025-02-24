package com.stonksco.minitramways.logic.map;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;
import com.stonksco.minitramways.logic.map.buildings.*;
import com.stonksco.minitramways.logic.map.generation.BuildingsGenerator;
import com.stonksco.minitramways.logic.map.generation.PeopleGenerator;
import com.stonksco.minitramways.logic.map.lines.Line;
import com.stonksco.minitramways.logic.map.lines.LinePart;
import com.stonksco.minitramways.logic.map.lines.Tramway;
import com.stonksco.minitramways.logic.people.People;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class GameMap {

    // Grid
    private HashMap<Vector2, Cell> Grid = null;
    private Vector2 GridSize = null;

    // 
    private HashMap<Integer, Line> Lines;
    private HashMap<Integer, Area> Areas;
    private HashMap<Vector2, Station> Stations;

    // générateurs
    private BuildingsGenerator BuildingGen;
    private PeopleGenerator PeopleGen;


    public GameMap() {
        BuildingGen = new BuildingsGenerator(this);
        PeopleGen = new PeopleGenerator(this);
        Lines = new HashMap<>();
        Areas = new HashMap<>();
        Stations = new HashMap<>();
    }

    // Construct the gid with a given size
    private void InitGrid(int width, int height) {

        Game.Debug(1,"Initializing grid...");

        GridSize = new Vector2(width,height);
        Grid = new HashMap<>();

        for(int i =0; i<height; i++) {
            for(int j = 0; j<width; j++) {
                Vector2 v = new Vector2(j,i);
                Grid.put(v,new Cell(v));
                Game.Debug(3,"Added cell at "+ v);
            }
        }
        Game.Debug(1,"Grid initialized with "+Grid.size()+" cells.");
    }

    /// Initialize the map
    public void Init() {
        InitGrid(36,23);
        Lines = new HashMap<>();
        InitAreas();
        BuildingGen.InitBuildings();

    }

    public Cell GetCellAt(Vector2 v) {
        return Grid.get(v);
    }

    public Vector2 GetGridSize() {
        return GridSize.clone();
    }


    private ArrayList<Integer> linesToUpdate;

    /**
     * Create a new line from start to end and return its ID
     */
    public Line CreateLine(Vector2 start, Vector2 end) {
        linesToUpdate = new ArrayList<>();
        Line createdLine = null;

        Game.Debug(2,"Line modification initiated from "+start+" to "+end);
        int creationMode = 0; 
        // Mode of creation according to the context:
        // 0 = impossible to create ;
        // 1 = Creation of a new full line or creation of a new line with the second existing station

        if(GetBuildingAt(start)==null || GetBuildingAt(start) instanceof Station) {
            // Create a new line
            if(GetBuildingAt(end) instanceof Station) {
                // We create a new line with the second existing station
                    creationMode = 1;

            } else if(GetBuildingAt(end) == null) {
                // We create a new line with the second station to be created
                creationMode = 1;
            }
        }

        if(creationMode==1) { 
            
                Line l = new Line(start,end,Lines.size());
                Lines.put(Lines.size(),l);
                // Generate intersections if necessary
                linesToUpdate.add(l.GetID());
                HashMap<LinePart,Vector2> intersections =  CheckIntersections(l.GetFirstPart());
                ProcessIntersections(intersections,l.GetFirstPart());
                // For each intersection, we add the line concerned to the list of lines to be updated
                for(Map.Entry e : intersections.entrySet()) {
                    linesToUpdate.add(GetIdOf(((LinePart)e.getKey()).GetLine()));
                }
                Game.Debug(1,"Line "+l.GetID()+" created from "+start+" to "+end);
                createdLine = l;
        }

        Game.Debug(2,"Updated lines : "+linesToUpdate);

        if(linesToUpdate.size()>0)
            People.UpdateGraph();

        return createdLine;
    }

    /**
     * Extend the line from start to end and return the ID of the line affected
     */
    public Line ExtendLine(Vector2 start, Vector2 end, Integer lineID) {
        linesToUpdate = new ArrayList<>();
        Line extendedLine = null;

        Line l2 = null;
        if(lineID!=null) {
            l2 = Lines.get(lineID);
        } else {
            l2 = ((Station)GetCellAt(start).GetBuilding()).GetLines()[0];
        }


        Game.Debug(2,"Line modification initiated from "+start+" to "+end);
        int creationMode = 0; 
         // Mode of creation according to the context:
        // 0 = impossible to create ;
        // 2 = Extension of an existing line from the first station or extension of an existing line, but the second station already exists

        if(GetBuildingAt(start) instanceof Station) {
            if(GetBuildingAt(end) != null) {
                // A building is on the second cell
                if(GetBuildingAt(end) instanceof Station) {
                    
                    creationMode = 2;
                }
            } else {
                // The second cell is empty
                creationMode = 2;
            }

        }

        if(creationMode==2) {
            // Extend the line

            LinePart lp2 = l2.Extend(start,end);
            if(lp2!=null) {
                // Generate intersections if necessary
                linesToUpdate.add(l2.GetID());
                HashMap<LinePart,Vector2> intersections2 =  CheckIntersections(lp2);
                ProcessIntersections(intersections2,lp2);
            
                for(Map.Entry e : intersections2.entrySet()) {
                    linesToUpdate.add(GetIdOf(((LinePart)e.getKey()).GetLine()));
                }
                Game.Debug(1,"Line "+l2.GetID()+" extended from "+start+" to "+end);
                extendedLine=l2;
            }
        }

        Game.Debug(2,"Updated lines : "+linesToUpdate);

        if(linesToUpdate.size()>0)
            People.UpdateGraph();

        return extendedLine;
    }


    public ArrayList<Integer> GetLinesToUpdate() {
        if(linesToUpdate==null)
            linesToUpdate=new ArrayList<>();

        ArrayList<Integer> temp = (ArrayList<Integer>) linesToUpdate.clone();
        linesToUpdate.clear();
        return temp;
    }


    private void ProcessIntersections(HashMap<LinePart,Vector2> intersections, LinePart with) {

        
        for(Line l : Lines.values()) {
            // We give it each intersection, so that the linespart concerned is divided
            for(Map.Entry e : intersections.entrySet()) {
                if(GetBuildingAt((Vector2)e.getValue())==null) {
                    if (l.Divide(((LinePart) e.getKey()).GetStartPos(), ((LinePart) e.getKey()).GetEndPos(), (Vector2) e.getValue()) != null) {
                        //with.divide(with.getStartPos(),with.getEndPos(),(Vector2)e.getValue());
                        Station s = AddStation((Vector2) e.getValue());
                        s.AddLine(l);
                        Game.Debug(2, "Intersection processed : Station created at " + e.getValue());
                    }
                }
            }
        }


        // Create a list of all the contact details of intersections and then orders them from the closest to the most distant from the starting point
        List<Vector2> divisionsByDistance = new ArrayList<>();
        for(Vector2 v : intersections.values()) {
            if(!divisionsByDistance.contains(v))
                divisionsByDistance.add(v);
        }

        Collections.sort(divisionsByDistance, Comparator.comparingDouble(new ToDoubleFunction<Vector2>() {
            @Override
            public double applyAsDouble(Vector2 v) {
                return Vector2.Distance(v, with.GetStartPos());
            }
        }));


        if(divisionsByDistance.size()>0)
            Game.Debug(1,"Divisions needed for "+with+" : "+divisionsByDistance);
        else
            Game.Debug(1,"No divisions needed for "+with);


        LinePart partToDivide = with;
        for(Vector2 v : divisionsByDistance) {
            partToDivide = partToDivide.Divide(partToDivide.GetStartPos(),partToDivide.GetEndPos(),v);
            if(partToDivide!=null) {
                ((Station) GetBuildingAt(v)).AddLine(with.GetLine());
                for (People p : People.GetAll()) {
                    p.AddIntersectionBetween(v, partToDivide.GetStartPos(), partToDivide.GetEndPos());
                }
            }
        }

        People.UpdateGraph();


    }

    /**
     * Loop around the map to find the closest empty cell or station
     */
    public Vector2 GetClosestEmptyOrStation(Vector2 from) {
        Game.Debug(2,"Searching for closest linkable cell from "+from+"...");
        Vector2 res = null;
        from = from.Round();

        // (di, dj) is a vector - direction in which we move right now
        int dx = 1;
        int dy = 0;
        // length of current segment
        int segment_length = 1;

        // current position (i, j) and how much of current segment we passed
        int x = (int)from.GetX();
        int y = (int)from.GetY();
        int segment_passed = 0;
        for (int k = 0; k < 15; ++k) {

            Cell checkedCell = GetCellAt(new Vector2(x,y));
            if(checkedCell!=null) {
                if(checkedCell.GetBuilding()==null || checkedCell.GetBuilding() instanceof Station)
                    res = checkedCell.GetCoordinates();
            }

            if(res!=null)
                break;

            // make a step, add 'direction' vector (di, dj) to current position (i, j)
            x += dx;
            y += dy;

            ++segment_passed;

            if (segment_passed == segment_length) {
                // done with current segment
                segment_passed = 0;

                // 'rotate' directions
                int buffer = dx;
                dx = -dy;
                dy = buffer;

                // increase segment length if necessary
                if (dy == 0) {
                    ++segment_length;
                }
            }
        }
        Game.Debug(2,"Found "+res);
        return res;
    }

    public Station AddStation(Vector2 at) {
        if(Stations==null)
            Stations = new HashMap<>();
        Station s = new Station(GetCellAt(at));
        GetCellAt(at).SetBuilding(s);
        Stations.put(at,s);
        return s;
    }


    private ArrayList<LinePart> alreadyChecked;

    /**
     * Check if interesection should be made between the new part and the other lines
     */
    public HashMap<LinePart,Vector2> CheckIntersections(LinePart newPart) {
        if(alreadyChecked==null)
            alreadyChecked=new ArrayList<>();

        HashMap<LinePart,Vector2> intersections = new HashMap<>();
        if(newPart!=null) {
            for (Line l : Lines.values()) {
                for (LinePart lp : l.GetParts()) {
                    if (newPart != null && newPart != lp &&
                            !newPart.GetStartPos().equals(lp.GetEndPos()) &&
                            !newPart.GetStartPos().equals(lp.GetStartPos()) &&
                            !newPart.GetEndPos().equals(lp.GetStartPos()) &&
                            !newPart.GetEndPos().equals(lp.GetEndPos())) {
                        Vector2 v = Vector2.GetIntersectionOf(newPart.GetStartPos(), newPart.GetEndPos(), lp.GetStartPos(), lp.GetEndPos());
                        if (v != null)
                            intersections.put(lp, GetClosestEmptyOrStation(v.Round()));
                    }
                }
                alreadyChecked.add(newPart);
            }
        }
        Game.Debug(1,"Found intersections for part "+newPart+" : "+intersections.values());
        return intersections;
    }

    public Building GetBuildingAt(Vector2 cell) {
        return GetCellAt(cell).GetBuilding();
    }


    public HashMap<Integer, Area>  GetAreas() {
        return Areas;
    }

    public void InitAreas() {
        Areas = new HashMap<>();

        ArrayList<Cell> list1 = new ArrayList<>();
        for (int x = 13; x <= 23; x++) {
            list1.add(Game.Get().GetMap().Grid.get(new Vector2(x, 0)));
            list1.add(Game.Get().GetMap().Grid.get(new Vector2(x, 1)));
        }
        for (int x = 14; x <= 24; x++) {
            for (int y = 2; y <= 4; y++) {
            list1.add(Game.Get().GetMap().Grid.get(new Vector2(x, y)));
            }
        }
        list1.add(Game.Get().GetMap().Grid.get(new Vector2(15, 5)));
        list1.add(Game.Get().GetMap().Grid.get(new Vector2(22, 5)));
        list1.add(Game.Get().GetMap().Grid.get(new Vector2(23, 5)));

        Area shop = new Area(list1, AreaTypes.shopping);

        ArrayList<Cell> list2 = new ArrayList<>();
        for (int x = 29; x <= 35; x++) {
            for (int y = 6; y <= 15; y++) {
            if (!(x == 28 && (y == 7 || y == 8 || y == 12 || y == 13 || y == 14))) {
                list2.add(Game.Get().GetMap().Grid.get(new Vector2(x, y)));
            }
            }
        }

        Area office = new Area(list2,AreaTypes.office);

        ArrayList<Cell> list3 = new ArrayList<>();
        for(int x =0; x<6; x++) {
            for(int y=6; y<23; y++)
                list3.add(Game.Get().GetMap().Grid.get(new Vector2(x,y)));
        }
        Area residence = new Area(list3,AreaTypes.residential);

        Areas.put(0,shop);
        Areas.put(1,office);
        Areas.put(2,residence);

    }


    public Area GetAreaOf(Vector2 pos) {
        Area res = null;
        for (Area a : Areas.values()) {
            if(a.IsIn(pos)){
                res=a;
                break;
            }

        }
        return res;
    }

    // Return the ID of the line in parameter
    public int GetIdOf(Line l) {
        return l.GetID();
    }

    public Area GetAreas(int i ){
        return Areas.get(i);
    }

    public int GetNombreArea(){
        return Areas.size();

    }

    public HashMap<BuildingEnum,ArrayList<Vector2>> GetBuildings(){
        HashMap<BuildingEnum,ArrayList<Vector2>> HM = new HashMap<>();
        ArrayList<Cell> TempC = new ArrayList<>();
        ArrayList<Vector2> TempV = new ArrayList<>();

        for(Area A : Areas.values()) {

            switch(A.GetType()) {
                case residential:
                    TempV=new ArrayList<>();
                    TempC = A.GetCells();
                    for(int b=0; b<TempC.size();b++){
                        if(TempC.get(b).GetBuilding()!=null) {
                            TempV.add(TempC.get(b).GetCoordinates());

                        }
                    }
                    HM.put(BuildingEnum.HOUSE,TempV);
                    break;

                case office:
                    TempV=new ArrayList<>();
                    TempC = A.GetCells();
                    for(int b=0; b<TempC.size();b++){
                        if(TempC.get(b).GetBuilding()!=null) {
                            TempV.add(TempC.get(b).GetCoordinates());

                        }
                    }
                    HM.put(BuildingEnum.OFFICE,TempV);
                    break;

                case shopping:
                    TempV=new ArrayList<>();
                    TempC = A.GetCells();
                    for(int b=0; b<TempC.size();b++){
                        if(TempC.get(b).GetBuilding()!=null) {
                            TempV.add(TempC.get(b).GetCoordinates());

                        }
                    }
                    HM.put(BuildingEnum.SHOP,TempV);
                    break;

            }
            TempC=null;
        }
        return HM;
    }


    public List<Map.Entry<Vector2,Vector2>> GetPartsVectorsOf(int lineID) {
        return Lines.get(lineID).GetPartsVectors();
    }

    public String GetLineString(int lineID) {
        String res = "LINE "+lineID+" DOES NOT EXIST";
        Line l = Lines.get(lineID);
        if(l!=null)
            res=l.toString();
        return res;
    }

    public ArrayList<Vector2> GetStations() {
        ArrayList<Vector2> res = new ArrayList<>();
        res.addAll(Stations.keySet());
        return res;
    }

    public boolean IsAtExtremity(Vector2 pos) {
        boolean res=false;
        for(Line l : Lines.values()) {
            res=l.IsAtExtremity(pos);
            if(res) break;
        }
        return res;
    }

    public void Update() {
        // update tramways
        for(Line l : Lines.values()) {
            l.Update();
        }
        // building generation and people generation
        BuildingGen.BuildingsGeneration();
        PeopleGen.PeopleGeneration();

    }

    public ArrayList<Tramway> GetTramsOf(int lineID) {
        return this.Lines.get(lineID).GetTrams();
    }

    public int GetFirstIndexOf(int lineID) {
        return this.Lines.get(lineID).GetFirstIndex();
    }

    public int GetLastIndexOf(int lineID) {
        return this.Lines.get(lineID).GetLastIndex();
    }

    /**
     * Return the number of people at the given position
     */
    public int GetAmountOf(Vector2 pos) {
        int res = -1;
        if(this.GetCellAt(pos).GetBuilding() instanceof PlaceToBe) {
            res = GetCellAt(pos).GetBuilding().Amount();
        }
        return res;
    }

    private SplittableRandom randGen = new SplittableRandom();


    public House GetRandomHouse() {
        House res = null;

        Area a = null;
        while(a==null) {
            int nb = (int)(randGen.nextInt(Areas.size()));
            a = Areas.get(nb);
            if(a.GetType() != AreaTypes.residential)
                a=null;
        }

        res = (House)a.GetRandomBuilding();
        return res;
    }

   // Get a random target building
    public Building getRandomTarget() {
        Building res = null;

        Area a = null;
        while(a==null) {
            int nb = (int)(randGen.nextInt(Areas.size()));
            a = Areas.get(nb);
            if(a.GetType() == AreaTypes.residential)
                a=null;
        }

        res = a.GetRandomBuilding();
        return res;
    }

    public int LinesCount() {
        return Lines.size();
    }

    
    public Vector2 GetClosestStation(Vector2 from) {
        double min = Double.POSITIVE_INFINITY;
        Vector2 res = null;
        for(Vector2 s : Stations.keySet()) {
            double d = Vector2.Distance(from,s);
            if(d<min && d<((Station)GetBuildingAt(s)).Radius()) {
                min = d;
                res = s;
            }
        }
        return res;
    }

    public PlaceToBe VectorToPlace(Vector2 v) {
        return GetCellAt(v).GetBuilding();
    }

    public boolean IsOnLine(Vector2 station, int lineID) {
        boolean res = false;
        Station s = Stations.get(station);

        if(s!=null) {
            for(Line l : s.GetLines()) {
                if (l.GetID() == lineID) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    public LinePart GetPartBetween(Vector2 pos1, Vector2 pos2) {
        LinePart res = null;
        for(Line l : Lines.values()) {
            for(LinePart lp : l.GetParts()) {
                if( (lp.GetStartPos().equals(pos1) && lp.GetEndPos().equals(pos2)) || (lp.GetStartPos().equals(pos2) && lp.GetEndPos().equals(pos1))) {
                    res=lp;
                    break;
                }
            }
            if(res!=null)
                break;
        }
        return res;
    }

    public Integer[] GetLinesID() {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.addAll(Lines.keySet());
        return ids.toArray(new Integer[0]);
    }


    
    // Destroy the station at the given position
    // Return the updated lines
    public ArrayList<Integer> DestroyStation(Vector2 stationtodestroy) {
        ArrayList<Integer> updatedLines = new ArrayList<>();

        if(GetBuildingAt(stationtodestroy) instanceof Station) {
            Station s = (Station) GetBuildingAt(stationtodestroy);
            boolean canContinue = true;

            // If the station is at the extremity of a line, we can destroy it
            for(Line l : s.GetLines()) {
                if(!l.IsAtExtremity(stationtodestroy))
                    canContinue=false;
            }

            if(canContinue) {
                boolean canDestroy = true;
                for(Line l : s.GetLines()) {
                    boolean trimmed = l.Trim(stationtodestroy);
                    if(trimmed) {
                        updatedLines.add(l.GetID());
                        linesToUpdate.add(l.GetID());
                        s.RemoveLine(l.GetID());
                        // Reimburse 15 if the station is trimmed
                        Game.Get().AddMoney(15);
                    } else {
                        canDestroy=false;
                    }
                }
                if(canDestroy) {
                    Stations.remove(stationtodestroy);
                    // Reimburse 30 if the station is destroyed
                    Game.Get().AddMoney(30);
                }
                // If the station is not connected to any line, we can destroy it
                for(Station s_ : Stations.values()) {
                    if(s_.GetLines().length==0)
                        Stations.remove(s_.GetCoordinates());
                }
            }
        }



        return updatedLines;
    }

    public Integer LineFromExtremity(Vector2 firstcell) {
        Line l = null;
        for(Line l_ : Lines.values()) {
            if(l_.IsAtExtremity(firstcell)) {
                l=l_;
                break;
            }
        }
        Integer res = null;
        if(l!=null)
            res = l.GetID();
        return res;
    }
}
