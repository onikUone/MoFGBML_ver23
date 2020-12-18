package jmetal6.problem.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.problem.doubleproblem.impl.AbstractDoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;

@SuppressWarnings("serial")
public class Rosenbrock extends AbstractDoubleProblem {

  /**
   * Constructor
   * Creates a default instance of the Rosenbrock problem
   *
   * @param numberOfVariables Number of variables of the problem
   */
  public Rosenbrock(Integer numberOfVariables) {
    setNumberOfVariables(numberOfVariables);
    setNumberOfObjectives(1);
    setNumberOfConstraints(0) ;
    setName("Rosenbrock");

    List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
    List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

    for (int i = 0; i < getNumberOfVariables(); i++) {
      lowerLimit.add(-5.12);
      upperLimit.add(5.12);
    }

    setVariableBounds(lowerLimit, upperLimit);
  }

  /** Evaluate() method */
  @Override
  public void evaluate(DoubleSolution solution) {
    int numberOfVariables = getNumberOfVariables() ;

    double[] x = new double[numberOfVariables] ;

    for (int i = 0; i < numberOfVariables; i++) {
      x[i] = solution.getVariable(i) ;
    }

    double sum = 0.0;

    for (int i = 0; i < numberOfVariables - 1; i++) {
      double temp1 = (x[i] * x[i]) - x[i + 1];
      double temp2 = x[i] - 1.0;
      sum += (100.0 * temp1 * temp1) + (temp2 * temp2);
    }

    solution.setObjective(0, sum);
  }
}

