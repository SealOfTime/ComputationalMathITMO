package ru.sealofthetime.mathcalc.lab1;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Matrix implements Cloneable{
    protected final double[][] matrixData;

    public int rows(){
        return matrixData.length;
    }

    public int cols(){
        return matrixData[0].length;
    }

    public Matrix(double[][] matrixData) {
        this.matrixData = matrixData;
    }

    public Matrix(double[] matrixData) {
        double[][] rotated = new double[matrixData.length][];
        for(int i = 0; i < rotated.length; i++)
            rotated[i] = new double[]{matrixData[i]};

        this.matrixData = rotated;
    }

    public Matrix swap(int row1, int row2) {
        var temp = matrixData[row1];
        matrixData[row1] = matrixData[row2];
        matrixData[row2] = temp;
        return this;
    }

    public Matrix swapCols(int col1, int col2){
        double temp;
        for(int row = 0; row < matrixData.length; row++){
            temp = matrixData[row][col1];
            matrixData[row][col1] = matrixData[row][col2];
            matrixData[row][col2] = temp;
        }
        return this;
    }

    public double norm(){
        return Arrays.stream(this.matrixData)
                .mapToDouble(row-> Arrays.stream(row).sum())
                .max()
                .getAsDouble();
    }

    public Matrix minor(int row, int column) {
        int size = this.matrixData.length - 1;
        var newData = new double[size][size];
        for (int i = 0; i < this.matrixData.length; i++)
            for (int j = 0; j < this.matrixData.length; j++)
                if (i != row && j != column)
                    newData[i < row ? i : i - 1][j < column ? j : j - 1] = this.matrixData[i][j];

        return new Matrix(newData);
    }
    public Matrix transpond(){
        var transpondedMatrix = new double[this.matrixData[0].length][];
        for(int i = 0; i < transpondedMatrix.length; i++)
            transpondedMatrix[i] = new double[this.matrixData.length];

        return new Matrix(transpondedMatrix);
    }
    @Override
    public String toString() {
        var rowJoiner = new StringJoiner("\n ");
        Arrays.stream(this.matrixData)
                .map(row -> {
                    var joiner = new StringJoiner(", ");
                    DoubleStream.of(row)
                            .mapToObj(d->String.format("% -25.15f", d))
                            .forEach(joiner::add);
                    return joiner.toString();
                })
                .forEach(rowJoiner::add);
        return "[" + rowJoiner.toString() + "]";
    }

    @Override
    protected Matrix clone(){
        return new Matrix(deepCopy(this.matrixData));
    }

    private static double[][] deepCopy(double[][] original) {
        if (original == null) {
            return null;
        }

        final double[][] result = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
}
