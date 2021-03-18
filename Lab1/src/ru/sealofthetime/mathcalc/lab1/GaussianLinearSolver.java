package ru.sealofthetime.mathcalc.lab1;

public class GaussianLinearSolver extends LinearEquationSolver {

    protected int swapCount = 0;
    public GaussianLinearSolver(Matrix matrix, Matrix freeMembers, double approximation) {
        super(matrix, freeMembers, approximation);
    }

    @Override
    public boolean solve() {
        if(transformMatrixToTriangularForm())
            return false;
        if(determinant() == 0)
            return false;

        backtrace();
        return true;
    }

    public void backtrace(){
        var matrixData = matrix.matrixData;
        var freeMembersData = freeMembers.matrixData;
        var answerData = new double[matrixData.length][];
        for(int i =0; i < answerData.length; i++)
            answerData[i] = new double[1];

        for(int n = answerData.length-1; n >= 0; n--){
            for(int j = n+1; j < matrixData[n].length; j++)
                freeMembersData[n][0] -= answerData[j][0]*matrixData[n][j];
            answerData[n][0] = freeMembersData[n][0]/matrixData[n][n];
        }

        answer = new Matrix(answerData);
    }

    protected double determinant(){
        double det = 1;
        for(int i = 0; i < matrix.rows(); i++)
            det *= matrix.matrixData[i][i];
        return det * (swapCount % 2 == 0 ? 1 : -1);
    }

    protected boolean transformMatrixToTriangularForm(){
        double[][] matrixData = matrix.matrixData;
        for(int i = 0; i < matrix.rows(); i++){
            var diagonalElement = matrixData[i][i];
            if(isZero(diagonalElement))
                if(!fixZeroCoefficients(i)) // Если не получилось найти ненулевой элемент столбца i-ого, то уравнение не решаем
                    return true;
            eliminateDiagonalCoefficient(i);
        }
        return false;
    }

    private void eliminateDiagonalCoefficient(int indexOfDiagonalCoef){
        double[][] matrixData = matrix.matrixData;
        for(int k = indexOfDiagonalCoef+1; k < matrix.rows(); k++){ //Исключаем коэффициент в k-м уравнении, начиная со следующего
            var relationCoefficient = matrixData[k][indexOfDiagonalCoef]/matrixData[indexOfDiagonalCoef][indexOfDiagonalCoef];
            matrixData[k][indexOfDiagonalCoef] = 0; //matrixData[k][i] = matrixData[k][i] - relationCoefficient*matrixData[i][i]
            for(int j = indexOfDiagonalCoef+1; j < matrix.cols(); j++)
                matrixData[k][j] = matrixData[k][j] - matrixData[indexOfDiagonalCoef][j]*relationCoefficient;
            freeMembers.matrixData[k][0] = freeMembers.matrixData[k][0] - freeMembers.matrixData[indexOfDiagonalCoef][0]*relationCoefficient;
        }
    }

    private boolean fixZeroCoefficients(int indexOfDiagonalCoef){
        double[][] matrixData = matrix.matrixData;
        for(int equationNumberToSwap = indexOfDiagonalCoef+1; equationNumberToSwap < matrixData.length; equationNumberToSwap++){
            if(!isZero(matrixData[equationNumberToSwap][indexOfDiagonalCoef])){
                swapEquations(equationNumberToSwap, indexOfDiagonalCoef);
                return true;
            }
        }
        return false;
    }

    private void swapEquations(int equationNumberToSwap, int indexOfDiagonalCoef){
        matrix.swap(equationNumberToSwap, indexOfDiagonalCoef);
        freeMembers.swap(equationNumberToSwap, indexOfDiagonalCoef);
        swapCount++;
    }

    private boolean isZero(double value){
        return Math.abs(value) < approximation;
    }

}
