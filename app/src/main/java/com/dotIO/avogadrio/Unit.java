package com.dotIO.avogadrio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rikab Gambhir on 12/29/2016.
 */

public class Unit {

    public enum unitType {
        LENGTH,
        MASS,
        TIME,
        FORCE,
        ENERGY,
        VOLUME,
        PRESSURE,
        AMOUNT,
        TEMPERATURE
    }

    final String[] lengthUnits = {"Meters", "Kilometers", "Centimeters", "Millimeters","Nanometers", "Angstroms", "Inches","Feet", "Miles"};
    final String[] lengthSymbols = {"m", "km", "cm", "mm", "nm", "Å", "in", "ft", "mi"};
    final double[] lengthConversions = {1.0, 0.001, 100.0, 1000.0, 1000000.0, 10000000.0, 39.3701, 3.2808, 0.000621371};

    final String[] massUnits = {"Kilograms", "Grams", "Milligrams", "Pounds", "Tons", "Ounces"};
    final String[] massSymbols = {"kg", "g", "mg", "lbs", "t", "oz"};
    final double[] massConversions = {1.0, 1000.0, 1000000.0, 2.20462, 0.00110231, 35.274};

    final String[] timeUnits = {"Seconds", "Minutes", "Hours", "Milliseconds", "Days", "Years"};
    final String[] timeSymbols = {"s", "min", "hr", "ms", "d", "yr"};
    final double[] timeConversions = {1.0, 1/60.0, 1/3600.0, 1000.0, 1/86164.1, 1.0/31536000 };

    final String[] forceUnits = {"Newtons", "Pounds"};
    final String[] forceSymbols = {"N", "lbs"};
    final double[] forceConversions = {1.0, 0.224809};

    final String[] energyUnits = {"Joules", "Kilojoules", "Calories", "Kilocalories", "Kilowatt-hours", "British Thermal Units", "Electron volts", "Mega-electron volts"};
    final String[] energySymbols = {"J", "kJ", "cal", "Cal", "kWh", "BTU", "eV", "MeV"};
    final double[] energyConversions = {1.0, 0.001, 1/4.187, 0.001/4.187, 1.0/3600000, 1.0/1055, Double.parseDouble("6.242E+18"), Double.parseDouble("6.242E+12") };

    final String[] volumeUnits = {"Meters Cubed", "Centimeters Cubed", "Liters", "Milliliters", "Gallons"};
    final String[] volumeSymbols = {"m^3", "cm^3", "L", "mL", "gal"};
    final double[] volumeConversions = {1.0, 1000000.0, 1000.0, 1000000.0, 264.172};

    final String[] pressureUnits = {"Pascals", "Kilopascals", "Atmospheres", "Bar", "Torr", "Millimeters of Mercury", "Pounds per Square Inch"};
    final String[] pressureSymbols = {"Pa", "kPa", "atm", "bar", "torr", "mmHg", "psi"};
    final double[] pressureConversions = {1.0, 0.001, Double.parseDouble("9.86923e-6"), 0.00001, 0.00750062, 0.00750062, 0.000145038};

    final String[] amountUnits = {"Moles", "Atoms"};
    final String[] amountSymbols = {"mol", "atom"};
    final double[] amountConversions = {1.0, Double.parseDouble("6.022141e23")};

    double value;
    double baseValue;
    String unit;
    unitType type;
    int unitTypeIndex;

    final String[][] units = {lengthUnits, massUnits, timeUnits, forceUnits, energyUnits, volumeUnits, pressureUnits, amountUnits};
    final String[][] symbols = {lengthSymbols, massSymbols, timeSymbols, forceSymbols, energySymbols, volumeSymbols, pressureSymbols, amountSymbols};
    final double[][] conversions = {lengthConversions, massConversions, timeConversions, forceConversions, energyConversions, volumeConversions, pressureConversions, amountConversions};

    public Unit(String s){
        String[] values = parse(s);
        value = Double.parseDouble(values[0]);
        String[] spacedUnits = values[1].split(" ");
        for (int i = 0; i < spacedUnits.length;i++) if (unit == null) unit = findUnit(spacedUnits[i], value);
        unitTypeIndex = Arrays.asList(unitType.values()).indexOf(type);
    }

    public Unit(double v, String s) {
        value = v;
        String[] spacedUnits = s.split(" ");
        for (int i = 0; i < spacedUnits.length; i++)
            if (unit == null) unit = findUnit(spacedUnits[i], value);
        unitTypeIndex = Arrays.asList(unitType.values()).indexOf(type);
    }

    public static boolean isUnit(String s){
        boolean result = false;
        try{
            Unit u = new Unit(s);
            if (!u.type.equals("") && u.unit != null)result = true;
        }
        catch(Exception e){}
        return result;
    }


    public void setType(unitType type) {
        this.type = type;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    public String[] parse(String s){
        String[] answer = new String[2];
        String format = "(\\d+(?:\\.\\d+)?)\\s*([\\w\\d\\s]+)";
        Pattern u = Pattern.compile(format);
        Matcher m = u.matcher(s);
        while (m.find()){
            answer[0] = m.group(1);
            answer[1] = m.group(2);
        }
        return answer;
    }

    public static ArrayList<Unit> parseUnits(String s){
        ArrayList<Unit> unitList = new ArrayList<>();
        String format = "(\\d+(?:\\.\\d+)?)\\s*([\\w\\d\\s]+)";
        Pattern u = Pattern.compile(format);
        Matcher m = u.matcher(s);
        while (m.find()) {
            Unit un = new Unit(m.group(1) + " " + m.group(2));
            unitList.add(un);
        }
        return unitList;
    }


    public String findUnit(String s, double v){
        s = s.toLowerCase();
        //Check Length Units
        if (s.equals("foot")) {
            setType(unitType.values()[0]);
            setBaseValue(v / lengthConversions[5]);
            return units[0][5];
        }

        for (int un = 0; un < units.length; un++){
            for (int i = 0; i < units[un].length; i++){
                if (
                        s.equals(units[un][i].toLowerCase())
                                || s.equals(units[un][i].substring(0,units[un][i].length() -1).toLowerCase())
                                || s.equals(symbols[un][i].toLowerCase())
                        )
                {
                    setBaseValue(v / conversions[un][i]);
                    setType(unitType.values()[un]);
                    return units[un][i];
                }
            }
        }
        return null;
    }

    public String toString(){
        return "" + this.value + " " + this.unit;
    }

    public double[] convert(){
        double[] answers = new double[units[unitTypeIndex].length];
        for (int i = 0; i < answers.length; i++){
            answers[i] = baseValue *conversions[unitTypeIndex][i];
        }
        return answers;
    }

}
