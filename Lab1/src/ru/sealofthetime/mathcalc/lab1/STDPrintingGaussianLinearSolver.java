package ru.sealofthetime.mathcalc.lab1;

public class STDPrintingGaussianLinearSolver extends GaussianLinearSolver{
    private Matrix originalMatrix;
    private Matrix originalFreeMembers;
    public STDPrintingGaussianLinearSolver(Matrix matrix, Matrix freeMembers, double approximation) {
        super(matrix, freeMembers, approximation);
        originalMatrix = matrix.clone();
        originalFreeMembers = freeMembers.clone();
    }

    @Override
    protected boolean transformMatrixToTriangularForm() {
        var result = !super.transformMatrixToTriangularForm();
        System.out.println("Треугольный вид матрицы:");
        System.out.println(matrix);
        System.out.println("Преобразованный столбец b:");
        System.out.println(freeMembers);
        return !result;
    }

    @Override
    protected double determinant() {
        var det = super.determinant();
        System.out.println("Детерминант матрицы:");
        System.out.printf("% -25.15f\n", det);
        System.out.println("Количество перестановок: ");
        System.out.printf("%d\n", this.swapCount);

        return det;
    }

    @Override
    public boolean solve() {
        var result = super.solve();
        if(result) {
            var answer = answer();
            System.out.println("Столбец неизвестных:");
            System.out.println(answer);
            System.out.println("Невязки:");

            for (int i = 0; i < originalMatrix.rows(); i++) {
                double bActual = 0;
                for (int j = 0; j < originalMatrix.cols(); j++)
                    bActual += originalMatrix.matrixData[i][j] * answer.matrixData[j][0];
                double residual = originalFreeMembers.matrixData[i][0] - bActual;
                System.out.println(residual);
            }
        }

        return result;
    }
}
