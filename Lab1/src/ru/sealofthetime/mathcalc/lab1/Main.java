package ru.sealofthetime.mathcalc.lab1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        String fileName = System.getProperty("matrixFile", null);
        Scanner sc;

        if(fileName != null) {
            try {
                sc = new Scanner(new FileInputStream(fileName));
            }catch(FileNotFoundException e){
                System.out.println("File with the name " + fileName + " was not found.");
                System.exit(-1);
                return;
            }
        }else{
            sc = new Scanner(System.in);
            System.out.println("-DmatrixFile has not been specified, reverting to manual matrix input");
            System.out.println("Input a list of double values, decimal separator is a dot, separated by whitespaces, then proceed to repeat");
            System.out.println("as much times as the number of elements entered, this way filling up a square matrix row by row");
        }
        var matrix = new Matrix(readMatrix(sc));

        System.out.println("Now enter free members column in one line separated by whitespace: ");
        var freeMembers = new Matrix(parseLine(sc.nextLine()));

        System.out.println("Матрица коэффициентов:");
        System.out.println(matrix);

        System.out.println("Столбец свободных членов:");
        System.out.println(freeMembers);

        var gaussianSolver = new STDPrintingGaussianLinearSolver(matrix,freeMembers,0.0000000000000001d);
        var matrixClone = matrix.clone();
        var ies = new IterativeEquationSolver(matrixClone, freeMembers.clone(), 0.0005d);
        if(!gaussianSolver.solve())
            System.out.println("У матрицы нет решения >:(");

//        if(!ies.solve())
//            System.out.print("Метод простых итераций не сходится :( Норма: " + matrixClone.norm());
//        else
//            System.out.println(ies.answer);
    }

    private static double[][] readMatrix(Scanner sc){
        int lineNumber = 0;
        int size = 0;
        double[][] matrixData = new double[0][0];

        do {
            var values = parseLine(sc.nextLine())   ;
            if(values == null)
                System.exit(-1);

            if(lineNumber == 0) {
                size = values.length;
                matrixData = new double[size][size];
            }

            if(values.length != size){
                System.out.println("You must enter null values specifically");
                System.exit(-1);
            }

            matrixData[lineNumber] = values;
            lineNumber++;
        }while(lineNumber < size);

        return matrixData;
    }

    public static double[] parseLine(String line){
        var rawValues = line.split(" ");
        try {
            return Stream.of(rawValues).mapToDouble(Double::parseDouble).toArray();
        }catch(NumberFormatException e){
            System.out.println("You have betrayed me with invalid input. Bye.");
            System.out.println(e.toString());
            System.exit(-1);
            return null;
        }
    }
}
