package com.dotIO.avogadrio;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.fraction.Fraction;

/**
 * Created by Rikab Gambhir on 12/11/2016.
 */

public class ChemicalEquation {

    String equation;
    Map<Molecule, Integer> LHS = new LinkedHashMap<>();
    Map<Molecule, Integer> RHS = new LinkedHashMap<>();
    Map<Element, Integer> LHSatoms = new LinkedHashMap<>();
    Map<Element, Integer> RHSatoms = new LinkedHashMap<>();
    Map<Molecule, Integer> LHSBalanced = new LinkedHashMap<>();
    Map<Molecule, Integer> RHSBalanced = new LinkedHashMap<>();

    public ChemicalEquation(String e) throws TimeoutException{
        equation = e;
        String[] sidesBalanced = equation.split("(=)|(->)");
        LHS = parse(sidesBalanced[0]);
        RHS = parse(sidesBalanced[1]);
        LHSatoms = totalAtoms(LHS);
        RHSatoms = totalAtoms(RHS);
        //while(!isBalanced())
        if (!isBalanceable()) {throw new TimeoutException();}

        if (!isBalanced()) {
        try {
            balance();
        } catch (TimeoutException e1) {
            throw new TimeoutException();
        } catch (Exception e2) {
            balance2();                     //In case balance() method does not work
        }
        }
    }

    private Map<Molecule, Integer> parse(String expression){
        Map<Molecule, Integer> molecules = new LinkedHashMap<>();
        String pattern = "(\\d*)([a-zA-Z0-9\\(\\)]+)";
        Pattern m = Pattern.compile(pattern);
        Matcher r = m.matcher(expression);
        while (r.find()){
            Molecule molecule = new Molecule(r.group(2));
            if (r.group(1).equals("")){
                molecules.put(molecule, 1);
            }else {
                Integer coefficient = Integer.parseInt(r.group(1));
                molecules.put(molecule, coefficient);
            }
        }
        return molecules;
    }

    private String trimCoefficients(String e){
        String pattern = "(\\d*)([a-zA-Z0-9\\(\\)]+)";
        Pattern m = Pattern.compile(pattern);
        Matcher r = m.matcher(e);
        while (r.find()){
            e = e.replaceFirst(r.group(1),"");
        }
        return e;
    }

    public void printMatrix(double[][] m){
        try{
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";

            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    str += m[i][j] + "\t";
                }

                System.out.println(str + "|");
                str = "|\t";
            }

        }catch(Exception e){System.out.println("Matrix is empty!!");}
    }

    private Map<Element, Integer> totalAtoms(Map<Molecule, Integer> side){
        Map<Element, Integer> total = new HashMap<>();
        for (Map.Entry<Molecule, Integer> entry : side.entrySet()) {
            Molecule m = entry.getKey();
            int v = entry.getValue();
            for (Map.Entry<Element, Integer> atom : m.getElements().entrySet()) {
                Element e = atom.getKey();
                int u = atom.getValue();
                int oldValue = 0;
                for (Map.Entry<Element, Integer> tot : total.entrySet()){
                    if (tot.getKey().isEqual(e)){
                        e = tot.getKey();
                        oldValue = tot.getValue();
                    }
                }
                total.put(e, u * v + oldValue);
            }
        }
        return total;
    }

    public boolean isBalanceable(){
        for (Map.Entry<Element, Integer> atom : LHSatoms.entrySet()) {
            Element e = atom.getKey();
            int u = atom.getValue();
            Element re = e;
            if (RHSatoms.get(e) == null){
                return false;
            }
        }
        return true;
    }

    public boolean isBalanced(){

        for (Map.Entry<Element, Integer> atom : LHSatoms.entrySet()) {
            Element e = atom.getKey();
            int u = atom.getValue();
            Element re = e;
            for (Map.Entry<Element, Integer> tot : RHSatoms.entrySet()){
                if (tot.getKey().isEqual(re)){
                    re = tot.getKey();
                }
            }
            if (u != RHSatoms.get(re))
                return false;
        }
        return true;
    }

    public String toString(){
        String formula = "";
        for (Map.Entry<Molecule, Integer> entry : LHS.entrySet()) {
            Molecule m = entry.getKey();
            int v = entry.getValue();
            if (v != 1)
                formula += v + m.toString() + " + ";
            else
                formula += m.toString() + " + ";
        }
        formula = formula.substring(0, formula.length() - 3);
        formula += " \u2192 ";

        for (Map.Entry<Molecule, Integer> entry : RHS.entrySet()) {
            Molecule m = entry.getKey();
            int v = entry.getValue();
            if (v != 1)
                formula += v + m.toString() + " + ";
            else
                formula += m.toString() + " + ";
        }
        formula = formula.substring(0, formula.length() - 3);

        return formula;
    }



    public void balance() throws TimeoutException{
        int size = LHS.size() + RHS.size();
        int rows = LHSatoms.size();

        double matrix[][] = new double[0][0];

        if (rows + 1 == size) {
            matrix = new double[rows][size];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = 0;
                }
            }
        }

        if (rows + 1 < size) {
            matrix = new double[size][size + 1];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size + 1; j++) {
                    if (i >= rows && i == j) {
                        matrix[i][j] = 1;
                    } else matrix[i][j] = 0;
                }
            }
        }

        if (rows + 1 > size) {
            matrix = new double[rows + 1][rows + 2];
            for (int i = 0; i < rows + 1; i++) {
                for (int j = 0; j < rows + 1; j++) {
                    if (i >= size && i == j) {
                        matrix[i][j] = 1;
                    } else matrix[i][j] = 0;
                }
                matrix[i][rows + 1] = 1;
            }
        }

        addToMatrixR(LHS, LHSatoms, matrix);                                // Populate Matrix
        addToMatrixP(RHS, LHSatoms, matrix, LHS.size());
        //printMatrix(matrix);

        double[] answer = gauss_jordan(matrix);                    // Use Gauss-Jordan Reduction to solve for final Ratios

        int[] denominators = new int[answer.length];                // Create array of denominators
        findDenoms(answer, denominators);

        int lcm = lcm(denominators);                                // Find the least common multiple of all answers' denominators

        int[] values = reduceDoubles(answer, lcm);                    // Convert Double array to Int Array

        int gcf = findGCF(values);                                    // Find GCF of values then divide all values by that
        reduceInts(values, gcf);

        if (allZeros(values)){
            for (int i = 0; i < values.length; i++){
                values[i] = 1;
            }
        }


        int counter = 0;
        for(Map.Entry<Molecule, Integer> entry : LHS.entrySet()){
            LHS.put(entry.getKey(), values[counter]);
            counter++;
        }
        for(Map.Entry<Molecule, Integer> entry : RHS.entrySet()){
            RHS.put(entry.getKey(), values[counter]);
            counter++;
        }
        LHSatoms = totalAtoms(LHS);
        RHSatoms = totalAtoms(RHS);

        if (!isBalanced()) balance2();
    }

    private boolean allZeros(int[] values){
        for (int i = 0; i < values.length; i++){
            if (values[i] != 0) return false;
        }
        return true;
    }

    private void balance2() throws TimeoutException{
        Random ran = new Random();
        int MAX_COEFF = 24;
        int[] coefficients = new int[LHS.size() + RHS.size()];
        double start = System.currentTimeMillis();
        double end = start + 7.5*1000;
        while (!isBalanced()){
            int counter = 0;
            for (Map.Entry<Molecule, Integer> entry : LHS.entrySet()) {
                Molecule m = entry.getKey();
                int v = entry.getValue();
                int x = ran.nextInt(MAX_COEFF) + 1;
                LHSBalanced.put(m, x * v);
                coefficients[counter] = x * v;
                counter++;
            }
            for (Map.Entry<Molecule, Integer> entry : RHS.entrySet()) {
                Molecule m = entry.getKey();
                int v = entry.getValue();
                int x = ran.nextInt(MAX_COEFF) + 1;
                RHSBalanced.put(m, x * v);
                coefficients[counter] = x * v;
                counter++;
            }
            LHSatoms = totalAtoms(LHSBalanced);
            RHSatoms = totalAtoms(RHSBalanced);

            if(System.currentTimeMillis() > end ){
                throw new TimeoutException();
            }
        }

        if (allZeros(coefficients)){
            for (int i = 0; i < coefficients.length; i++) coefficients[i] = 1;
        }

        int gcd = gcd(coefficients);
        for (int i = 0; i < coefficients.length; i++){
            coefficients[i] /= gcd;
        }
        int counter = 0;
        for (Map.Entry<Molecule, Integer> entry : LHSBalanced.entrySet()){
            Molecule m = entry.getKey();
            LHSBalanced.put(m, coefficients[counter]);
            counter++;
        }
        for (Map.Entry<Molecule, Integer> entry : RHSBalanced.entrySet()){
            Molecule m = entry.getKey();
            RHSBalanced.put(m, coefficients[counter]);
            counter++;
        }
        LHSatoms = totalAtoms(LHSBalanced);
        RHSatoms = totalAtoms(RHSBalanced);
        LHS = LHSBalanced;
        RHS = RHSBalanced;

    }

    private boolean isIntArray(double[] d){
        for (int i=0; i < d.length; i++){
            if(d[i]%1 != 0){
                return false;
            }
        }
        return true;
    }

    private static double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    private int findGCF(int[] values) {
        int a = values[0];
        int b = values[1];
        int temp;
        int gcf = Integer.MAX_VALUE;
        for(int i = 2; i < values.length; i++){
            temp = gcd(a,b);
            if (temp < gcf)
                gcf = temp;
            a = b;
            b = values[i];
        }
        return gcf;
    }

    /**
     * Reduce integer array by the GCF
     *
     * @param values
     * @param gcf
     */
    private void reduceInts(int[] values, int gcf) {
        for(int i = 0; i < values.length; i++){
            values[i] /= gcf;
        }
    }

    /**
     * Converts an array of doubles to integer equivalencies by multiplying them all by the LCM of their denominators
     *
     * @param answer - Array of Doubles
     * @param lcm   -  	Least Common Multiple of all of the doubles.  All values will be multiplied by this to return an int value
     */
    private static int[] reduceDoubles(double[] answer, int lcm){
        int[] values = new int[answer.length];
        for(int i = 0; i < answer.length; i++){
            answer[i] = Math.abs(answer[i]);
            answer[i] *= lcm;
            values[i] = Math.round((int)answer[i]);
        }
        return values;
    }

    /**
     * Finds all of the denominators from parameter d and return puts them into parameter j
     *
     * @param d
     * @param j
     */
    private static void findDenoms(double[] d, int[] j) {
        Fraction fraction;
        for(int i = 0; i < d.length; i++){
            fraction = new Fraction(d[i]);
            j[i] = fraction.getDenominator();
        }
    }

    /**
     * Finds the least common multiple of the values in the inputed double Array
     * @param denominators - an array of ints (will be modified)
     */
    private int lcm(int[] denominators) {
        int a = denominators[0];
        int b = denominators[1];
        int temp = 0;
        int lcm = 0;
        for(int i = 2; i < denominators.length; i++){
            temp = lcm(a,b);
            if (temp > lcm)
                lcm = temp;
            a = b;
            b = denominators[i];
        }

        return lcm;
    }

    /**
     * Finds the least common multiple of two different numbers.
     *
     * @param a
     * @param b
     * @return
     */
    public static int lcm(double a, double b){
        return (int)(a * b / gcd(a,b));
    }

    /**
     * Returns the Greatest Common Denominator using Euclid's algorithm
     *
     * @param a
     * @param b
     * @return
     */
    public static int gcd(double a, double b){
        double temp;
        while(b != 0){
            temp = b;
            b = a % b;
            a = temp;
        }
        return (int) a;
    }

    private static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int gcd(int[] a){
        int gcd = a[0];
        for(int i = 1; i < a.length; i++)
            gcd = gcd(gcd, a[i]);
        return gcd;
    }


    public static int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }


    public String printMap(){
        String formula = "";
        for (Map.Entry<Element, Integer> entry : LHSatoms.entrySet()) {
            Element e = entry.getKey();
            int v = entry.getValue();
            formula += e.getName() + ": " + v + "\n";
        }
        formula += "------------";
        for (Map.Entry<Element, Integer> entry : RHSatoms.entrySet()) {
            Element e = entry.getKey();
            int v = entry.getValue();
            formula += e.getName() + ": " + v + "\n";
        }
        return formula;
    }

    private void addToMatrixP(Map<Molecule, Integer> rhs, Map<Element,Integer> rhsatoms, double[][] matrix, int size) {
        int i = 0;
        for(Map.Entry<Molecule, Integer> entry : rhs.entrySet()){
            int j = 0;
            for (Map.Entry<Element, Integer> element : rhsatoms.entrySet()){
                Element currentE = element.getKey();
                Molecule m = entry.getKey();
                int atomsInMolecule = 0;
                for (Map.Entry<Element, Integer> atoms : m.elements.entrySet()){
                    if (atoms.getKey().isEqual(currentE)){
                        atomsInMolecule = atoms.getValue();
                    }
                }
                matrix[j][i + size] =  -1* atomsInMolecule;
                j++;
            }
            i++;
        }
    }

    private void addToMatrixR(Map<Molecule, Integer> rhs, Map<Element,Integer> rhsatoms, double[][] matrix) {
        int i = 0;
        for(Map.Entry<Molecule, Integer> entry : rhs.entrySet()){
            int j = 0;
            for (Map.Entry<Element, Integer> element : rhsatoms.entrySet()){
                Element currentE = element.getKey();
                Molecule m = entry.getKey();
                int atomsInMolecule = 0;
                for (Map.Entry<Element, Integer> atoms : m.elements.entrySet()){
                    if (atoms.getKey().isEqual(currentE)){
                        atomsInMolecule = atoms.getValue();
                    }
                }
                matrix[j][i] = atomsInMolecule;
                j++;
            }
            i++;
        }
    }

    public double[] gauss_jordan(double[][] ma){
        double[][] matrix = new double[ma.length][ma[0].length];  			//Copy array
        for(int i = 0; i < ma.length; i++){
            for(int j = 0; j < ma[0].length; j++){
                matrix[i][j] = ma[i][j];
            }
        }

        double[] answer = new double[ma.length + 1];							//Create answer array
        for(int i = 0; i < answer.length; i++){
            answer[i] = -1;
        }

        int col;
        double factor;
        double[] temp;
        double scale;

        for(int row = 0; row < matrix.length; row++){
            col = row;                                    //cell identity on diagonal
            for(int x = row + 1; x < matrix.length; x++){
                if(Math.abs(matrix[row][col]) < Math.abs(matrix[x][col])){
                    temp = matrix[row];
                    matrix[row] = matrix[x];
                    matrix[x] = temp;
                }
            }

            if (matrix[row][col] == 0){			// Equation Deemed Unsolvable
                return null;
            }
            factor = 1.0 / matrix[row][col];              // diagonal's inverse (make diagonal 1)

            for(int y = 0; y < matrix[0].length; y++) {
                matrix[row][y] *= factor;                 		// Mulitiply row by diagonal's inverse
            }

            for(int x = row + 1; x < matrix.length; x++){
                scale = matrix[x][col]/matrix[row][col];		// Calculate scale for row multiplication
                for(int y = 0; y < matrix[0].length; y++){
                    matrix[x][y] -= scale*matrix[row][y];
                }
            }
        }

        for(int row = matrix.length - 1; row > -1; row--){
            col = row;                               			//cell identity on diagonal
            for(int x = (row - 1); x > -1; x--){
                scale = matrix[x][col] / matrix[row][col];			//Divide by 0 error
                for(int y = 0; y < matrix[0].length; y++){
                    matrix[x][y] -= scale*matrix[row][y];
                }
            }
        }

        for(int row = 0; row < matrix.length; row++){
            answer[row] = matrix[row][matrix[0].length - 1];
        }
        answer[answer.length - 1] = 1;

        return answer;
    }

    public static boolean isChemicalEquation(String s){
        return s.contains("=") || s.contains("->");
    }

    private int getValue(Molecule m){
       Map<Molecule, Integer> all = new LinkedHashMap<>();
        all.putAll(LHS);
        all.putAll(RHS);
        return all.get(m);
    }

    public double stoichiometry(double input, Molecule in, Molecule out){
        return input * getValue(out) / getValue(in);
    }
}


