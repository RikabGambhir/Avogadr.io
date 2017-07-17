package com.dotIO.avogadrio;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rikab Gambhir on 12/10/2016.
 */

public class Molecule {

    PeriodicTable periodicTable = new PeriodicTable();
    String molecule;
    Map<Element, Integer> elements = new LinkedHashMap<>();
    String name;

    public Molecule(String formula) {
        molecule = "";
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (Character.isDigit(c) && c != '1') {
                molecule += subscript(c);
            } else if (c != '1'){
                molecule += c;
            }
        }
        elements = parse(formula);
        name = "";
        formula = map2String(getElements());
        molecule = "";
        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (Character.isDigit(c) && c != '1') {
                molecule += subscript(c);
            } else if (c != '1'){
                molecule += c;
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){return name;}

    private char subscript(char digit) {
        switch (digit) {
            case '1':
                return '\u2081';
            case '2':
                return '\u2082';
            case '3':
                return '\u2083';
            case '4':
                return '\u2084';
            case '5':
                return '\u2085';
            case '6':
                return '\u2086';
            case '7':
                return '\u2087';
            case '8':
                return '\u2088';
            case '9':
                return '\u2089';
            case '0':
                return '\u2080';
        }
        return '0';
    }

    public static boolean isMolecule(String s){
        boolean result = false;
        try{
            Molecule m = new Molecule(s);
            if(m.getElements().size() != 0
                    && s.indexOf(' ') == -1)result = true;
        }
        catch(Exception e){}
        return result;
    }

//    private String preProcesses(String formula){
//        String pattern = "([A-Z][a-z]*)(\\d*)";
//        Pattern pat = Pattern.compile(pattern);
//        Matcher r = pat.matcher(newFormula);
//        while (r.find()) {
//
//        }
//    }

    private Map<Element, Integer> parse(String formula) {
        String newFormula = formula;
        Map<Element, Integer> elementList = new LinkedHashMap<>();
        String paranthesis = "(\\([a-zA-z0-9]*\\))(\\d+)";
        Pattern paran = Pattern.compile(paranthesis);
        Matcher m = paran.matcher(formula);
        while (m.find()) {
            String subFormula = m.group(1);
            int multiplier = Integer.parseInt(m.group(2));
            Map<Element, Integer> subList = parse(subFormula);
            Map<Element, Integer> newList = new LinkedHashMap<>();
            for (Map.Entry<Element, Integer> entry : subList.entrySet()) {
                Element e = entry.getKey();
                int v = entry.getValue();
                newList.put(e, multiplier * v);
            }
            String replacer = map2String(newList);
            newFormula = m.replaceFirst(replacer);

        }

        String pattern = "([A-Z][a-z]*)(\\d*)";
        Pattern pat = Pattern.compile(pattern);
        Matcher r = pat.matcher(newFormula);
        while (r.find()) {
            int prev = 0;
            try{prev = elementList.get(periodicTable.getElement(r.group(1)));}
            catch(Exception e){prev = 0;}
            if (r.group(2).equals("")) {
                elementList.put(periodicTable.getElement(r.group(1)), prev + 1);
            } else {
                elementList.put(periodicTable.getElement(r.group(1)), prev + Integer.parseInt(r.group(2)));
            }
        }
        return elementList;
    }

    public Map<Element, Integer> getElements() {
        return elements;
    }

    public String map2String(Map<Element, Integer> elementList) {
        String formula = "";
        for (Map.Entry<Element, Integer> entry : elementList.entrySet()) {
            Element e = entry.getKey();
            int v = entry.getValue();
            formula += e.getSymbol() + v;
        }
        return formula;
    }

    public String printMap() {
        String formula = "";
        for (Map.Entry<Element, Integer> entry : elements.entrySet()) {
            Element e = entry.getKey();
            int v = entry.getValue();
            formula += e.getName() + ": " + v + "\n";
        }
        return formula;
    }

    public String toString() {
        return molecule;
    }

    public Double getMass() {
        Double mass = 0.0;
        for (Map.Entry<Element, Integer> entry : elements.entrySet()) {
            Element e = entry.getKey();
            int v = entry.getValue();
            mass += e.getAtomicWeight() * v;
        }
        return mass;
    }

    public boolean isIonic() {
        boolean hasMetal = false;
        boolean hasNonmetal = true;
        for (Element e : elements.keySet()) {
            if (e.type.matches("Alkali Metal|Alkakine Earth Metal|Metal|Transition Metal|Lanthanide|Actinide")) {
                hasMetal = true;
            }
            if (e.type.matches("Nonmetal|Halogen")) {
                hasNonmetal = true;
            }
        }
        if (hasMetal && hasNonmetal) {
            return true;
        } else {
            return false;
        }
    }

    private void name() {
        name = "";
        if (isIonic()) {
            for (Element e : elements.keySet()) {
                if (e.type.matches("Alkali Metal|Alkakine Earth Metal|Metal|Transition Metal|Lanthanide|Actinide")) {
                    name += e.getName();
                }
                if (e.type.matches("Nonmetal|Halogen")) {
                    String anion = e.getName();
                    switch (anion) {
                        case "Hydrogen":
                        {name += " " + "Hydride";break;}
                        case "Nitrogen":
                        {name += " " + "Nitride";break;}
                        case "Oxygen":
                        {name += " " + "Oxide";break;}
                        case "Phosphorous":
                        {name += " " + "Phosphide";break;}
                        case "Sulfur":
                        {name += " " + "Sulfide";break;}
                        default:
                        {name += " " + e.getName().substring(0, e.getName().length() - 3) + "ide";break;}
                    }
                }
            }
        }
        //Assuming covalent if not ionic
        else{
            int counter = 0;
            for (Element e : elements.keySet()){
                if (counter == 0) {
                    if (elements.get(e) > 1) {
                        name += greekPrefix(elements.get(e)) + decapitalize(e.getName());
                    } else {
                        name += e.getName();
                    }
                    counter ++;
                }
                else{
                    String anion = greekPrefix(elements.get(e));
                    if(e.getName().substring(0,1).matches("A|E|I|O|U") && (elements.get(e) != 2 && elements.get(e) != 3) ){
                        anion = anion.substring(0,anion.length()-1);
                    }
                    switch (e.getName()) {
                        case "Hydrogen":
                        {anion += "hydride"; break;}
                        case "Nitrogen":
                        {anion +=  "nitride";
                            break;}
                        case "Oxygen":
                        {anion += "oxide";
                            break;}
                        case "Phosphorous":
                        {anion +=  "phosphide";
                            break;}
                        case "Sulfur":
                        {anion += "sulfide";
                            break;}
                        default:
                        {anion += decapitalize(e.getName().substring(0, e.getName().length() - 3)) + "ide";
                            break;}
                    }
                    name += " " + anion;
                }
            }
        }
    }

    public String greekPrefix(int i){
        switch(i){
            case 1: return "Mono";
            case 2: return "Di";
            case 3: return "Tri";
            case 4: return "Tetra";
            case 5: return "Penta";
            case 6: return "Hexa";
            case 7: return "Hepta";
            case 8: return "Octa";
            case 9: return "Nova";
            case 10:return "Deca";
            default: return "Poly";

        }
    }

    public static String decapitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }
        char c[] = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
}


