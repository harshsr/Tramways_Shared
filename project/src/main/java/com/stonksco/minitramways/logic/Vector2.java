package com.stonksco.minitramways.logic;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Vector2 {

    private double x;
    private double y;

    public Vector2(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public Vector2(int x, int y) {
        this.x= x;
        this.y= y;
    }


    public double GetX() {
        return x;
    }

    public void SetX(int x) {
        this.x = x;
    }

    public double GetY() {
        return y;
    }

    public void SetY(int y) {
        this.y = y;
    }

    public Vector2 Div(Vector2 v) {
        return new Vector2(x/v.GetX(),y/v.GetY());
    }

    public Vector2 Scale(Vector2 v) {
        return new Vector2(x*v.GetX(),y*v.GetY());
    }

    public Vector2 Scale(double s) {
        return new Vector2(x*s,y*s);
    }

    public Vector2 Sub(Vector2 v) {
        return new Vector2(x-v.GetX(),y-v.GetY());
    }

    public Vector2 Add(Vector2 v) {
        return new Vector2(x+v.GetX(),y+v.GetY());
    }

    public Vector2 Normalize() {
        return new Vector2(x/length(),y/length());
    }

    public double length() {
        return Math.sqrt(x*x+y*y);
    }

    public double Scalar(Vector2 v) {
        return x*v.GetX()+y*v.GetY();
    }

    public Vector2 clone() {
        return new Vector2(x,y);
    }

    @Override
    public String toString() {
        BigDecimal xFormatted = BigDecimal.valueOf(x);
        BigDecimal yFormatted = BigDecimal.valueOf(y);
        xFormatted.setScale(3, RoundingMode.HALF_UP);
        yFormatted.setScale(3, RoundingMode.HALF_UP);
        xFormatted.stripTrailingZeros();
        yFormatted.stripTrailingZeros();

        return "( "+ xFormatted +" , "+ yFormatted +" )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Double.compare(vector2.GetX(), GetX()) == 0 && Double.compare(vector2.GetY(), GetY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(GetX(), GetY());
    }

    /**
     * Returns the point of intersection between two segments
     * @param start1 Start point of the first segment
     * @param end1 End point of the first segment
     * @param start2 Start point of the second segment
     * @param end2 End point of the second segment
     */
    public static Vector2 GetIntersectionOf(Vector2 start1, Vector2 end1, Vector2 start2, Vector2 end2) {
        Vector2 res = null;
        double x1 = start1.GetX();
        double x2 = end1.GetX();
        double x3 = start2.GetX();
        double x4 = end2.GetX();
        double y1 = start1.GetY();
        double y2 = end1.GetY();
        double y3 = start2.GetY();
        double y4 = end2.GetY();

        double bx = x2 - x1;
        double by = y2 - y1;
        double dx = x4 - x3;
        double dy = y4 - y3;

        double b_dot_d_perp = bx * dy - by * dx;

        if (b_dot_d_perp != 0) {
            double cx = x3 - x1;
            double cy = y3 - y1;

            double t = (cx * dy - cy * dx) / b_dot_d_perp;

            if (t < 0 || t > 1) {
                return null;
            }

            double u = (cx * by - cy * bx) / b_dot_d_perp;

            if (u < 0 || u > 1) {
                return null;
            } else {
                double x = x1 + t * bx;
                double y = y1 + t * by;
                res = new Vector2(x,y);
            }

        }

    return res;
    }

    public static double Distance(Vector2 v1, Vector2 v2) {
        double dist = Math.sqrt(Math.pow(v2.x - v1.x,2) + Math.pow(v2.y - v1.y,2));
        if(dist<0)
            dist*=-1;
        return dist;
    }

    /**
     * Returns an abstract value representing the distance between two vectors
     * Used for comparison purposes
     * @param v1
     * @param v2
     * @return
     */
    public static double AbstractDistance(Vector2 v1, Vector2 v2) {
        double dist = Math.pow(v2.x - v1.x,2) + Math.pow(v2.y - v1.y,2);
        if(dist<0)
            dist*=-1;
        return dist;
    }

    public Vector2 Round() {
        return new Vector2(Math.round(x),Math.round(y));
    }

    public static Vector2 GetMidPoint(Vector2 v1, Vector2 v2) {
        double x = (v1.x+v2.x)/2d;
        double y = (v1.y+v2.y)/2d;
        return new Vector2(x,y);
    }

    public static ReadOnlyDoubleProperty GetMidPointProperty(ReadOnlyDoubleProperty x1, ReadOnlyDoubleProperty x2) {
       SimpleDoubleProperty res =  new SimpleDoubleProperty();
       res.bind((x1.add(x2)).divide(2d));
       return res;
    }

}
