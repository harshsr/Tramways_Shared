package com.stonksco.minitramways.logic.map.lines;

import com.stonksco.minitramways.logic.Game;
import com.stonksco.minitramways.logic.Vector2;

import java.util.ArrayList;
import java.util.Objects;

public class LinePart {

    private Vector2 StartStation;
    private Vector2 EndStation;

    private LinePart PRec;
    private LinePart Next;

    public Line GetLine() {
        return Line;
    }

    private final Line Line;

    public int GetStart() {
        return Start;
    }

    public int GetEnd() {
        return End;
    }

    private int Start;
    private int End;

    public Vector2 GetStartPos() {
        return StartStation.clone();
    }

    public Vector2 GetEndPos() {
        return EndStation.clone();
    }


    public LinePart(Line line, Vector2 start, Vector2 end, LinePart first) {
        this.Line = line;
        this.StartStation = start;
        this.EndStation = end;
        if(first==null)
            this.SetPos(0,100,0);
    }

    public LinePart GetNext() {
        return Next;
    }

    public LinePart GetLast() {
        LinePart res = this;
        if(Next!=null)
            res=Next.GetLast();
        return res;
    }

    public boolean Add(LinePart partToAdd) {
        boolean res = false;




            // case where we are on the right side of the line
            if(partToAdd.StartStation.equals(this.EndStation) && Next==null) {
                this.Next = partToAdd;
                partToAdd.PRec = this;
                partToAdd.SetPos(this.End,this.End+100,1);
                Game.Debug(3,"Line "+Line.GetID()+" extended from right.");
                res=true;
            } else if(partToAdd.EndStation.equals(this.StartStation) && PRec==null){
                // case where we are on the left side
                this.PRec = partToAdd;
                partToAdd.Next = this;
                Line.SetFirst(partToAdd);
                partToAdd.SetPos(this.Start-100,this.Start,2);
                if(partToAdd.Start<0) {
                    // We reverse the departure and arrival positions to keep the order throughout the line
                    //partToAdd.reversePositions();
                }
                Game.Debug(3,"Line "+Line.GetID()+" extended from left.");

                res=true;
            } else {
                if(Next!=null)
                    res = Next.Add(partToAdd);
            }

        if(!res)
            Game.Debug(1,"ERROR when trying to add part "+partToAdd+" to line "+Line.toString());
        return res;
    }

    /**
     * Divides a section in half according to an intermediate station
     * @param at Place where the intermediate station is built
     */
    public LinePart Divide(Vector2 start, Vector2 end ,Vector2 at) {
        LinePart res = null;


        if(at!=null) {
            if(StartStation.equals(start) && EndStation.equals(end)) {
                if(!at.equals(StartStation) && !at.equals(EndStation)) {
                    // Preliminary calculations for the adjustment of the tram position
                    double oldLength = Vector2.Distance(StartStation,EndStation);

                    // We recover all the trams which are currently on this section
                    ArrayList<Tramway> trams = new ArrayList<>();

                    for(Tramway t : Line.GetTrams()) {
                        if(t.GetLinePos()>=this.Start && t.GetLinePos()<this.End)
                            trams.add(t);
                    }



                    LinePart newPart = new LinePart(Line,at,end,Line.GetFirstPart());
                    newPart.Next = this.Next;
                    newPart.PRec = this;
                    this.Next = newPart;
                    this.EndStation = at;
                    this.Next.SetPos(this.End,this.End+100,1);
                    res=newPart;
                    Game.Debug(2,"Line part "+this+" divided at "+at);


                    // Adjustment of the tram position
                    // We calculate the new length of this section
                    double newLength = Vector2.Distance(this.StartStation,this.EndStation);
                    double intersectLinePos = 100d*(newLength/oldLength);

                    for(Tramway t : trams) {
                        // We calculate the new position on the section of each tram
                        double posInPercent = t.GetLinePos()%100;
                        double distanceToStart = oldLength*(posInPercent/100d);
                        double newLinePos;
                        if(distanceToStart<=newLength)
                            // Case where the tram is on the left of the division
                            newLinePos = this.Start+(100*(distanceToStart/newLength));
                        else
                            // Right of the division
                            newLinePos = this.End+(100*((distanceToStart-newLength)/(oldLength-newLength)));

                        Game.Debug(2,"Tramway moved from "+t.GetLinePos()+" to "+newLinePos+" after line part division.");
                        t.PositionAt(newLinePos);

                    }
                }

            } else {
                if (Next!=null)
                    res=Next.Divide(start,end,at);
            }
        } else {
            Game.Debug(1,"ERROR when trying to divide line part : intersection point is null");
        }


        return res;

    }

    /**
     * Redefines the position in the chain of this member, and of all other members by recursion
     * @param from 0 = from nowhere; 1 = from the previous one; 2 = from the next
     */
    private void SetPos(int start, int end, int from) {
        this.Start = start;
        this.End = end;

        if(from == 0) {
            if(Next!=null)
                Next.SetPos(end,end+100,1);
            if(PRec != null)
                PRec.SetPos(start,start-100,2);
        } else if(from == 1) {
            if(Next!=null)
                Next.SetPos(end,end+100,1);
        } else if(from == 2) {
            if(PRec!=null)
                PRec.SetPos(start,start-100,2);
        }

    }

    @Override
    public String toString() {
        return StartStation+"["+Start+"]----------["+End+"]"+EndStation;
    }

    public String ToStringFull() {
        String s = StartStation+"["+Start+"]----------["+End+"]";
        if(Next != null)
            s = s+Next.ToStringFull();
        else
            s+=EndStation;
        return s;
    }

    public ArrayList<LinePart> GetParts() {
        ArrayList<LinePart> parts = new ArrayList<>();
        parts.add(this);
        if(Next!=null)
            parts.addAll(Next.GetParts());
        return parts;
    }

    public void ReversePositions() {
        Vector2 temp = StartStation;
        StartStation = EndStation;
        EndStation=temp;
    }

    /**
     * Return the coordinates on the section according to the percentage provided in parameter
     * If the given position does not correspond to this section, it will be requested from the following section
     * 0% = starting station | 100% = arrival station
     */
    public Vector2 GetPosAt(double at) {
        Vector2 res = null;

        if(Start <= at && End > at) {
            double x = 0;
            double y =0;
            if(at==0)
                res = StartStation;
            else if(at==100)
                res = EndStation;
            else
            {
                y = EndStation.GetY()-(StartStation.GetY())*(at/100d)+(StartStation.GetY());
                x = EndStation.GetX()-(StartStation.GetX())*(at/100d)+(StartStation.GetX());
            }

            if(res==null)
                res = new Vector2(x,y);
        } else {
            if(Next!=null)
                res = Next.GetPosAt(at);
        }
        return res;
    }

    public double GetLength() {
        return Vector2.Distance(StartStation,EndStation);
    }


    public LinePart GetPartAt(double at) {
        LinePart res = null;
        if(Start<=at && End>at)
            res = this;
        else
            if(Next!=null)
                res = Next.GetPartAt(at);

        return res;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinePart linePart = (LinePart) o;
        return Start == linePart.Start && End == linePart.End && StartStation.equals(linePart.StartStation) && EndStation.equals(linePart.EndStation) && Line.equals(linePart.Line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StartStation, EndStation, Line, Start, End);
    }

    /**
     * Destroyed the station in parameter if this section is directly linked (non -recursive)
     * Dangerous: this method can cause exceptions if a tram is on the section
     */
    LinePart Destroy(Vector2 todestroy) {
        LinePart res = null;

        if(StartStation.equals(todestroy) && PRec==null) {
        // Destroy startStation
            if(Next!=null)
                Next.PRec=null;
            res = Next;
        } else if(EndStation.equals(todestroy) && Next==null) {
            // Destroy endStation
            if(PRec!=null)
                PRec.Next=null;
            res=PRec;
        }



        return res;
    }
}
