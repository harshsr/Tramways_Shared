package com.stonksco.minitramways.logic;

import java.util.Objects;

/**
 * 2D vector couple
 */
public class Vector2Couple {

    private Vector2 V1;
    private Vector2 V2;

    public Vector2Couple(Vector2 v1, Vector2 v2) {
        this.V1 = v1;
        this.V2 = v2;
    }

    public double Distance() {
        return Vector2.Distance(V1,V2);
    }

    public Vector2 GetV1() {
        return V1.clone();
    }

    public Vector2 GetV2() {
        return V2.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2Couple that = (Vector2Couple) o;
        return V1.equals(that.V1) && V2.equals(that.V2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(V1, V2);
    }

    @Override
    public String toString() {
        return "Vector2Couple{" +
                "v1=" + V1 +
                ", v2=" + V2 +
                '}';
    }
}
