package com.dotIO.avogadrio;

/**
 * Created by Rikab Gambhir on 12/10/2016.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PeriodicTable {

    ArrayList<Element> elements = new ArrayList<Element>();

    public PeriodicTable(){


        elements.add(new Element(1,"Hydrogen","H", 1.00794, 1, "Nonmetal", 2.2));
        elements.add(new Element(2,"Helium","He", 4.002602, 18, "Noble Gas"));
        elements.add(new Element(3,"Lithium","Li", 6.941, 1, "Alkali Metal", 0.98));
        elements.add(new Element(4,"Beryllium","Be", 9.012182, 2, "Alkaline Earth Metal", 1.57));
        elements.add(new Element(5,"Boron","B", 10.811, 13, "Metalloid", 2.04));
        elements.add(new Element(6,"Carbon","C", 12.0107, 14, "Nonmetal", 2.55));
        elements.add(new Element(7,"Nitrogen","N", 14.0067, 15, "Nonmetal", 3.04));
        elements.add(new Element(8,"Oxygen","O", 15.9994, 16, "Nonmetal", 3.44));
        elements.add(new Element(9,"Fluorine","F", 18.9984032, 17, "Halogen", 3.98));
        elements.add(new Element(10,"Neon","Ne", 20.1797, 18, "Noble Gas"));
        elements.add(new Element(11,"Sodium","Na", 22.98976928, 1, "Alkali Metal", 0.93));
        elements.add(new Element(12,"Magnesium","Mg", 24.305, 2, "Alkaline Earth Metal", 1.31));
        elements.add(new Element(13,"Aluminum","Al", 26.9815386, 13, "Metal", 1.61));
        elements.add(new Element(14,"Silicon","Si", 28.0855, 14, "Metalloid", 1.9));
        elements.add(new Element(15,"Phosphorus","P", 30.973762, 15, "Nonmetal", 2.19));
        elements.add(new Element(16,"Sulfur","S", 32.065, 16, "Nonmetal", 2.58));
        elements.add(new Element(17,"Chlorine","Cl", 35.453, 17, "Halogen", 3.16));
        elements.add(new Element(18,"Argon","Ar", 39.948, 18, "Noble Gas"));
        elements.add(new Element(19,"Potassium","K", 39.0983, 1, "Alkali Metal", 0.82));
        elements.add(new Element(20,"Calcium","Ca", 40.078, 2, "Alkaline Earth Metal", 1));
        elements.add(new Element(21,"Scandium","Sc", 44.955912, 3, "Transition Metal", 1.36));
        elements.add(new Element(22,"Titanium","Ti", 47.867, 4, "Transition Metal", 1.54));
        elements.add(new Element(23,"Vanadium","V", 50.9415, 5, "Transition Metal", 1.63));
        elements.add(new Element(24,"Chromium","Cr", 51.9961, 6, "Transition Metal", 1.66));
        elements.add(new Element(25,"Manganese","Mn", 54.938045, 7, "Transition Metal", 1.55));
        elements.add(new Element(26,"Iron","Fe", 55.845, 8, "Transition Metal", 1.83));
        elements.add(new Element(27,"Cobalt","Co", 58.933195, 9, "Transition Metal", 1.88));
        elements.add(new Element(28,"Nickel","Ni", 58.6934, 10, "Transition Metal", 1.91));
        elements.add(new Element(29,"Copper","Cu", 63.546, 11, "Transition Metal", 1.9));
        elements.add(new Element(30,"Zinc","Zn", 65.38, 12, "Transition Metal", 1.65));
        elements.add(new Element(31,"Gallium","Ga", 69.723, 13, "Metal", 1.81));
        elements.add(new Element(32,"Germanium","Ge", 72.64, 14, "Metalloid", 2.01));
        elements.add(new Element(33,"Arsenic","As", 74.9216, 15, "Metalloid", 2.18));
        elements.add(new Element(34,"Selenium","Se", 78.96, 16, "Nonmetal", 2.55));
        elements.add(new Element(35,"Bromine","Br", 79.904, 17, "Halogen", 2.96));
        elements.add(new Element(36,"Krypton","Kr", 83.798, 18, "Noble Gas"));
        elements.add(new Element(37,"Rubidium","Rb", 85.4678, 1, "Alkali Metal", 0.82));
        elements.add(new Element(38,"Strontium","Sr", 87.62, 2, "Alkaline Earth Metal", 0.95));
        elements.add(new Element(39,"Yttrium","Y", 88.90585, 3, "Transition Metal", 1.22));
        elements.add(new Element(40,"Zirconium","Zr", 91.224, 4, "Transition Metal", 1.33));
        elements.add(new Element(41,"Niobium","Nb", 92.90638, 5, "Transition Metal", 1.6));
        elements.add(new Element(42,"Molybdenum","Mo", 95.96, 6, "Transition Metal", 2.16));
        elements.add(new Element(43,"Technetium","Tc", 98.0, 7, "Transition Metal", 1.9));
        elements.add(new Element(44,"Ruthenium","Ru", 101.07, 8, "Transition Metal", 2.2));
        elements.add(new Element(45,"Rhodium","Rh", 102.9055, 9, "Transition Metal", 2.28));
        elements.add(new Element(46,"Palladium","Pd", 106.42, 10, "Transition Metal", 2.2));
        elements.add(new Element(47,"Silver","Ag", 107.8682, 11, "Transition Metal", 1.93));
        elements.add(new Element(48,"Cadmium","Cd", 112.411, 12, "Transition Metal", 1.69));
        elements.add(new Element(49,"Indium","In", 114.818, 13, "Metal", 1.78));
        elements.add(new Element(50,"Tin","Sn", 118.71, 14, "Metal", 1.96));
        elements.add(new Element(51,"Antimony","Sb", 121.76, 15, "Metalloid", 2.05));
        elements.add(new Element(52,"Tellurium","Te", 127.6, 16, "Metalloid", 2.1));
        elements.add(new Element(53,"Iodine","I", 126.90447, 17, "Halogen", 2.66));
        elements.add(new Element(54,"Xenon","Xe", 131.293, 18, "Noble Gas"));
        elements.add(new Element(55,"Cesium","Cs", 132.9054519, 1, "Alkali Metal", 0.79));
        elements.add(new Element(56,"Barium","Ba", 137.327, 2, "Alkaline Earth Metal", 0.89));
        elements.add(new Element(57,"Lanthanum","La", 138.90547, 3, "Lanthanide", 1.1));
        elements.add(new Element(58,"Cerium","Ce", 140.116, 19, "Lanthanide", 1.12));
        elements.add(new Element(59,"Praseodymium","Pr", 140.90765, 20, "Lanthanide", 1.13));
        elements.add(new Element(60,"Neodymium","Nd", 144.242, 21, "Lanthanide", 1.14));
        elements.add(new Element(61,"Promethium","Pm", 145.0, 22, "Lanthanide", 1.13));
        elements.add(new Element(62,"Samarium","Sm", 150.36, 23, "Lanthanide", 1.17));
        elements.add(new Element(63,"Europium","Eu", 151.964, 24, "Lanthanide", 1.2));
        elements.add(new Element(64,"Gadolinium","Gd", 157.25, 25, "Lanthanide", 1.2));
        elements.add(new Element(65,"Terbium","Tb", 158.92535, 26, "Lanthanide", 1.2));
        elements.add(new Element(66,"Dysprosium","Dy", 162.5, 27, "Lanthanide", 1.22));
        elements.add(new Element(67,"Holmium","Ho", 164.93032, 28, "Lanthanide", 1.23));
        elements.add(new Element(68,"Erbium","Er", 167.259, 29, "Lanthanide", 1.24));
        elements.add(new Element(69,"Thulium","Tm", 168.93421, 30, "Lanthanide", 1.25));
        elements.add(new Element(70,"Ytterbium","Yb", 173.054, 31, "Lanthanide", 1.1));
        elements.add(new Element(71,"Lutetium","Lu", 174.9668, 32, "Lanthanide", 1.27));
        elements.add(new Element(72,"Hafnium","Hf", 178.49, 4, "Transition Metal", 1.3));
        elements.add(new Element(73,"Tantalum","Ta", 180.94788, 5, "Transition Metal", 1.5));
        elements.add(new Element(74,"Wolfram","W", 183.84, 6, "Transition Metal", 2.36));
        elements.add(new Element(75,"Rhenium","Re", 186.207, 7, "Transition Metal", 1.9));
        elements.add(new Element(76,"Osmium","Os", 190.23, 8, "Transition Metal", 2.2));
        elements.add(new Element(77,"Iridium","Ir", 192.217, 9, "Transition Metal", 2.2));
        elements.add(new Element(78,"Platinum","Pt", 195.084, 10, "Transition Metal", 2.28));
        elements.add(new Element(79,"Gold","Au", 196.966569, 11, "Transition Metal", 2.54));
        elements.add(new Element(80,"Mercury","Hg", 200.59, 12, "Transition Metal", 2));
        elements.add(new Element(81,"Thallium","Tl", 204.3833, 13, "Metal", 2.04));
        elements.add(new Element(82,"Lead","Pb", 207.2, 14, "Metal", 2.33));
        elements.add(new Element(83,"Bismuth","Bi", 208.9804, 15, "Metal", 2.02));
        elements.add(new Element(84,"Polonium","Po", 210.0, 16, "Metalloid", 2));
        elements.add(new Element(85,"Astatine","At", 210.0, 17, "Noble Gas", 2.2));
        elements.add(new Element(86,"Radon","Rn", 222.0, 18, "Alkali Metal"));
        elements.add(new Element(87,"Francium","Fr", 223.0, 1, "Alkaline Earth Metal", 0.7));
        elements.add(new Element(88,"Radium","Ra", 226.0, 2, "Actinide", 0.9));
        elements.add(new Element(89,"Actinium","Ac", 227.0, 3, "Actinide", 1.1));
        elements.add(new Element(90,"Thorium","Th", 232.03806, 19, "Actinide", 1.3));
        elements.add(new Element(91,"Protactinium","Pa", 231.03588, 20, "Actinide", 1.5));
        elements.add(new Element(92,"Uranium","U", 238.02891, 21, "Actinide", 1.38));
        elements.add(new Element(93,"Neptunium","Np", 237.0, 22, "Actinide", 1.36));
        elements.add(new Element(94,"Plutonium","Pu", 244.0, 23, "Actinide", 1.28));
        elements.add(new Element(95,"Americium","Am", 243.0, 24, "Actinide", 1.3));
        elements.add(new Element(96,"Curium","Cm", 247.0, 25, "Actinide", 1.3));
        elements.add(new Element(97,"Berkelium","Bk", 247.0, 26, "Actinide", 1.3));
        elements.add(new Element(98,"Californium","Cf", 251.0, 27, "Actinide", 1.3));
        elements.add(new Element(99,"Einsteinium","Es", 252.0, 28, "Actinide", 1.3));
        elements.add(new Element(100,"Fermium","Fm", 257.0, 29, "Actinide", 1.3));
        elements.add(new Element(101,"Mendelevium","Md", 258.0, 30, "Actinide", 1.3));
        elements.add(new Element(102,"Nobelium","No", 259.0, 31, "Actinide", 1.3));
        elements.add(new Element(103,"Lawrencium","Lr", 262.0, 32, "Actinide"));
        elements.add(new Element(104,"Rutherfordium","Rf", 261.0, 4, "Transactinide"));
        elements.add(new Element(105,"Dubnium","Db", 262.0, 5, "Transactinide"));
        elements.add(new Element(106,"Seaborgium","Sg", 266.0, 6, "Transactinide"));
        elements.add(new Element(107,"Bohrium","Bh", 264.0, 7, "Transactinide"));
        elements.add(new Element(108,"Hassium","Hs", 267.0, 8, "Transactinide"));
        elements.add(new Element(109,"Meitnerium","Mt", 268.0, 9, "Transactinide"));
        elements.add(new Element(110,"Darmstadtium ","Ds ", 271.0, 10, "Transactinide"));
        elements.add(new Element(111,"Roentgenium ","Rg ", 272.0, 11, "Transactinide"));
        elements.add(new Element(112,"Copernicium ","Cn ", 285.0, 12, "Transactinide"));
        elements.add(new Element(113,"Ununtrium ","Uut ", 284.0, 13, ""));
        elements.add(new Element(114,"Ununquadium ","Uuq ", 289.0, 14, "Transactinide"));
        elements.add(new Element(115,"Ununpentium ","Uup ", 288.0, 15, ""));
        elements.add(new Element(116,"Ununhexium ","Uuh ", 292.0, 16, "Transactinide"));
        elements.add(new Element(117,"Ununseptium ","Uus ", 295.0, 17, ""));
        elements.add(new Element(118,"Ununoctium ","Uuo ", 294.0, 18, "Noble Gas"));

    }

    public Element getElement(int i){
        return elements.get(i-1);
    }

    public Element getElement(String s) {
        if (s.length() > 2) {
            for (int i = 0; i < elements.size(); i++) {
                String name = elements.get(i).getName();
                if (s.toUpperCase().equals(name.toUpperCase()))
                    return elements.get(i);

            }
        }
        else {
            for (int i = 0; i < elements.size(); i++) {
                String name = elements.get(i).getSymbol();
                if (s.equals(name))
                    return elements.get(i);
            }
        }
        return null;
    }

    public boolean isElement(String s){
        if (getElement(s) == null) return false;
        else return true;
    }
}
