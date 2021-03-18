package ru.sealofthetime.mathcalc.lab1;

public abstract class LinearEquationSolver {
    protected Matrix matrix;
    protected Matrix freeMembers;
    protected final double approximation;

    protected Matrix answer;

    public LinearEquationSolver(Matrix matrix, Matrix freeMembers, double approximation){
        this.matrix = matrix;
        this.freeMembers = freeMembers;
        this.approximation = approximation;
    }

    public Matrix answer(){
        if(answer == null)
            throw new IllegalStateException("Equation has not been (yet) solved.");

        return this.answer;
    };

    abstract boolean solve();

}
