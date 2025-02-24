package com.stonksco.minitramways.logic;

import com.stonksco.minitramways.logic.interactions.ClickStateMachine;
import com.stonksco.minitramways.logic.interactions.InteractionException;
import com.stonksco.minitramways.logic.interactions.states.AbstractClickState;
import com.stonksco.minitramways.logic.map.Area;
import com.stonksco.minitramways.logic.map.GameMap;
import com.stonksco.minitramways.logic.map.lines.Line;
import com.stonksco.minitramways.logic.map.lines.Tramway;
import com.stonksco.minitramways.logic.map.buildings.BuildingEnum;
import com.stonksco.minitramways.logic.people.People;
import com.stonksco.minitramways.views.Clock;

import java.util.*;

public class Game {

    private int Debug = 0;
    private int Satisfaction = 0; // Average satisfaction of all people. Between 0 and 100
    private LinkedHashMap<People,Integer> Satisfactions;
    private ClickStateMachine ClickSM;

    private static final Game Game = new Game();

    private Game() {
        Satisfactions = new LinkedHashMap<People, Integer>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                return size() > 40;
            }
        };
        ComputeSatisfaction();
        ClickSM = new ClickStateMachine();
    }

    public static Game Get() {
        return Game;
    }

    public void SetDebug(int debug) {
        this.Debug = debug;
        if(debug>0)
            Debug(debug,"DEBUG ENABLED WITH A LEVEL OF "+debug);
    }

    private GameMap Map;
    private Player Player;

    private boolean NeedPinsUpdate = false;

    public GameMap GetMap() {
        return Map;
    }

    public static void Debug(int level,String msg) {
        if(level <= Get().Debug)
            System.out.println("[DEBUG] : "+msg);
    }

    public void InitGame() {
        Map = new GameMap();
        Player = new Player();
        Map.Init();
        Satisfactions.put(null,50);
    }

    public int GetDebug() {
        return this.Debug;
    }

    public Integer CreateLine(Vector2 start, Vector2 end) {
        Integer res = null;

        res = Map.CreateLine(start, end).GetID();

        return res;
    }

    public Vector2 GetMapSize() {
        return Map.GetGridSize().clone();
    }

    public HashMap<Integer, Area> GetAreas(){

        return GetMap().GetAreas();

    }

    public HashMap<BuildingEnum,ArrayList<Vector2>> GetBuildings(){
        return this.GetMap().GetBuildings();
    }
    public List<Map.Entry<Vector2,Vector2>> GetPartsVectorsOf(int lineID) {
        return Map.GetPartsVectorsOf(lineID);
    }

    public ArrayList<Vector2> GetStations() {
        return Map.GetStations();
    }

    /**
     * Return true if the position is at a line end
     */
    public boolean IsAtExtremity(Vector2 pos) {
        return Map.IsAtExtremity(pos);
    }

    public void Update() {
        Map.Update();
        for(People p : People.GetAll()) {
            p.Update();
        }
        ComputeSatisfaction();
    }

    public int GetFirstIndexOf(int lineID) {
        return Map.GetFirstIndexOf(lineID);
    }

    public int GetLastIndexIOf(int lineID) {
        return Map.GetLastIndexOf(lineID);
    }

    public ArrayList<Tramway> GetTramsOf(int lineID) {
        return Map.GetTramsOf(lineID);
    }

    
    public int GetAmountOfPeople(Vector2 pos) {
        return Map.GetAmountOf(pos);
    }

    
    public ArrayList<People> GetPeopleAt(Vector2 at) {
        ArrayList<People> res = new ArrayList<>();
        for(People p : People.GetAll()) {
            if(p.GetCurrentPlace().GetCoordinates().equals(at))
                res.add(p);
        }
        return res;
    }

    public boolean NeedPinsUpdate() {
        boolean res = NeedPinsUpdate;
        NeedPinsUpdate = false;
        return res;
    }

    public void RequestPinsUpdate() {
        NeedPinsUpdate = true;
    }

    public void AddMoney(int nb) {
            Player.AddMoney(nb);
    }

    public int GetMoney() {
        return Player.GetMoney();
    }

    public Integer[] GetLinesID() {
        return Map.GetLinesID();
    }

    public void SendSatisfaction(People people, int val) {
        Satisfactions.put(people,val);
    }

    private long lastSatisfactionUpdate = 0;

    private void ComputeSatisfaction() {
        if(lastSatisfactionUpdate>5000000000l) {
            double s = 0;
            for(Map.Entry e : Satisfactions.entrySet()) {
                s+=(Integer)e.getValue();
            }
            Satisfaction = (int) (s/Satisfactions.size());
            Game.Debug(2,"Satisfaction updated to "+Satisfaction);
            lastSatisfactionUpdate=0;
        } else {
            lastSatisfactionUpdate+= Clock.Get().DeltaTime();
        }
    }

    public int GetSatisfaction() {
        return Satisfaction;
    }

    public AbstractClickState SendLeftClick(Vector2 at) throws InteractionException {
        return ClickSM.SendLeftClick(at);
    }

    public AbstractClickState SendRightClick(Vector2 at) throws InteractionException {
        return ClickSM.SendRightClick(at);
    }

    public AbstractClickState GetCurrentClickState() {
        return ClickSM.GetCurrentState();
    }

    public Integer ExtendLine(Vector2 v1, Vector2 v2, Integer id) {
        Line l = Map.ExtendLine(v1,v2,id);
        int res=-1;
        if(l!=null)
            res = l.GetID();
        return res;
    }

    public void DestroyStation(Vector2 stationtodestroy) {
        ArrayList<Integer> res = Map.DestroyStation(stationtodestroy);
        if(res!=null) {
            if(res.size()>0) {
                for(People p : People.GetAll()) {
                    p.destroyed(stationtodestroy);
                }
            }
        }
    }

    // Return the ID of a line of which Firstcell is an end
    public Integer LineFromExtremity(Vector2 firstcell) {
        return Map.LineFromExtremity(firstcell);
    }
}
