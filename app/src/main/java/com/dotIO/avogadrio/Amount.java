package com.dotIO.avogadrio;

/**
 * Created by Rikab Gambhir on 1/10/2017.
 */

public class Amount {

    Molecule molecule;
    Unit unit;
    String[] parts;
    double mass;
    double grams;
    double moles;

    public Amount(String s){
        parts = s.split(" ");
        molecule = new Molecule(s);
        try {unit = new Unit(s);}
        catch(Exception e){}
        mass = molecule.getMass();

        if (unit.type == Unit.unitType.AMOUNT){
            moles = unit.baseValue;
            grams = moles * mass;
        }
        else if (unit.type == Unit.unitType.MASS){
            grams = 1000 * unit.baseValue;
            moles = grams / mass;
        }
    }

    private Molecule findMolecule(){
        for (int i = 0; i < parts.length; i++){
            if (Molecule.isMolecule(parts[i])){
                return new Molecule(parts[i]);
            }
        }
        return null;
    }

    private String concatenateArray(String[] s){
        String result = "";
        for (int i = 0; i < s.length; i ++) result += s[i];
        return result;
    }

    public static boolean isAmount(String s){
        boolean result = false;
        try{
            Amount amt = new Amount(s);
            if (amt.molecule != null && !amt.molecule.toString().equals("") && Unit.isUnit(amt.unit.toString())) result = true;
        }
        catch(Exception e){}
        return result;
    }
}
