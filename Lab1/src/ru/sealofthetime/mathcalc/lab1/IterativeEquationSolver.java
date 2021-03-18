package ru.sealofthetime.mathcalc.lab1;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.BitSet;

public class IterativeEquationSolver extends LinearEquationSolver {
    private Matrix previousAnswers;
    public IterativeEquationSolver(Matrix matrix, Matrix freeMembers, double approximation) {
        super(matrix, freeMembers, approximation);
    }

    @Override
    public boolean solve() {
        if(matrix.norm() < 1d){
            normalizeEquations();
            answer = freeMembers.clone(); //задаем изначальное приближение
            previousAnswers = answer;
            var newAnswers = answer.matrixData;
            var prevAnswers = previousAnswers.matrixData;
            var coefficients = matrix.matrixData;
            do{
                prevAnswers = newAnswers;
                for(int row = 0; row < newAnswers.length; row++){
                    newAnswers[row][0] = freeMembers.matrixData[row][0];
                    for(int col = 0; col < newAnswers[0].length; col++){
                        if(row == col)
                            continue;
                        newAnswers[row][0] -= prevAnswers[col][0]*coefficients[row][col];
                    }
                }
            }while(calculateDeviation() > approximation);
            return true;
        }
        return false;
    }

    protected double calculateDeviation(){
        var previousAnswers = this.previousAnswers.transpond().matrixData[0];
        var newAnswers = this.answer.transpond().matrixData[0];

        double[] deviation = new double[newAnswers.length];
        for(int i = 0; i < newAnswers.length; i++)
            deviation[i] = Math.abs(newAnswers[i] - previousAnswers[i]);

        return Arrays.stream(deviation)
                .max()
                .getAsDouble();
    }

    protected void normalizeEquations(){
        for(int row = 0; row < this.matrix.matrixData.length; row++)
            for(int col = 0; col < this.matrix.matrixData[0].length; col++) {
                this.matrix.matrixData[row][col] /= this.matrix.matrixData[row][row];
                this.freeMembers.matrixData[col][0] /= this.matrix.matrixData[row][row]; //нормализуем строки матрицы слау
            }
    }

    protected boolean ensureDiagonalDominance(){
        var matrixData = this.matrix.matrixData;
        var columnsWithMaximum = new BitSet(matrixData[0].length);
        double max = 0;
        int maxIndex = -1;
        for(int row = 0; row < matrixData.length; row++){
            for(int column = 0; column < matrixData[0].length; column++)
                if(Math.abs(matrixData[row][column]) > Math.abs(max)){
                    max = matrixData[row][column];
                    maxIndex = column;
                }
            if(columnsWithMaximum.get(maxIndex))
                return false; //Если будет столбец с двумя максимумамив разных строках, то невозможно будет добиться диаг. дом.
            columnsWithMaximum.set(maxIndex);
            matrix.swapCols(row, maxIndex);
        }
        return true;
    }
}
