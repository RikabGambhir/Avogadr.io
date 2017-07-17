package com.dotIO.avogadrio;

import java.util.ArrayList;

/**
 * Created by Rikab Gambhir on 6/19/2017.
 */

public class HalfReaction {

    private ArrayList<Molecule> molecules;

    public HalfReaction(String s){
        molecules = parse(s);
    }

    public ArrayList<Molecule> parse(String s) {
        ArrayList<Molecule> result = new ArrayList<>();
        String[] splitUp = s.split(" ");
        for (int i = 0; i < splitUp.length; i++) {
            if (Molecule.isMolecule(splitUp[i])) {
                result.add(new Molecule(splitUp[i]));
            }
        }
        return result;
    }

    public ArrayList getMolecules(){ return molecules; }


    public static boolean isHalfReaction(String s){
        boolean result = false;
        try{
            HalfReaction m = new HalfReaction(s);
            if(m.getMolecules().size() > 1)result = true;
        }
        catch(Exception e){}
        return result;
    }


}
