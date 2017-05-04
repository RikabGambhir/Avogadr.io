package com.dotIO.avogadrio;

import java.util.ArrayList;

/**
 * Created by Rikab Gambhir on 1/31/2017.
 */

public class Problem {


}

class ProblemParser{

    static ArrayList<Unit> parse(String s){
        return Unit.parseUnits(s);
    }

    static String[] sentences(String s){
        return s.split(".");
    }

}
