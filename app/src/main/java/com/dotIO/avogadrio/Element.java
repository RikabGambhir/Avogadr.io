package com.dotIO.avogadrio;

/**
 * Created by Rikab Gambhir on 12/10/2016.
 */

public class Element {

    int atomicNumber;
    String symbol;
    String name;
    Double atomicWeight;
    int group;
    String type;
    Double electronegativity;

    public Element(int Z, String Name, String Symbol, Double A, int g, String t, double e){
        atomicNumber = Z;
        name = Name;
        symbol = Symbol;
        atomicWeight = A;
        group = g;
        type = t;
        electronegativity = e;
    }

    public Element(int Z, String Name, String Symbol, Double A, int g, String t){
        atomicNumber = Z;
        name = Name;
        symbol = Symbol;
        atomicWeight = A;
        type = t;
        group = g;
        electronegativity = null;
    }

    public String getType() {
        return type;
    }

    public int getAtomicNumber(){
        return atomicNumber;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getAtomicWeight() {
        return atomicWeight;
    }

    public int getGroup() {
        return group;
    }

    public double getElectronegativity() {
        return electronegativity;
    }

    public boolean isEqual(Element e){
        if (e.getAtomicNumber() == atomicNumber) return true;
        else return false;
    }

    public String toString(){
        return name;
    }
}
