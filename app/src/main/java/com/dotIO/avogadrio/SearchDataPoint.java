package com.dotIO.avogadrio;

/**
 * Created by Rikab Gambhir on 5/20/2017.
 */

public class SearchDataPoint {

    public String searchType;
    public String searchValue;
    public String time;

    public SearchDataPoint(){}

    public SearchDataPoint(String searchType, String searchValue, String time){
        this.searchType = searchType;
        this.searchValue = searchValue;
        this.time = time;
    }
}
