package jmetal6.problem.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.problem.integerproblem.impl.AbstractIntegerProblem;
import jmetal6.solution.integersolution.IntegerSolution;

/**
 * Created by Antonio J. Nebro on 03/07/14.
 * Single objective problem for testing integer encoding.
 * Objective: minimizing the distance to value N
 */
@SuppressWarnings("serial")
public class NIntegerMin extends AbstractIntegerProblem {
  private int valueN ;

  public NIntegerMin() {
    this(10, 100, -100, +100);
  }

  /** Constructor */
  public NIntegerMin(int numberOfVariables, int n, int lowerBound, int upperBound)  {
    valueN = n ;
    setNumberOfVariables(numberOfVariables);
    setNumberOfObjectives(1);
    setName("NIntegerMin");

    List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
    List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

    for (int i = 0; i < getNumberOfVariables(); i++) {
      lowerLimit.add(lowerBound);
      upperLimit.add(upperBound);
    }

    setVariableBounds(lowerLimit, upperLimit);
  }

  /** Evaluate() method */
  @Override
  public void evaluate(IntegerSolution solution) {
    int approximationToN;

    approximationToN = 0;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      int value = solution.getVariable(i) ;
      approximationToN += Math.abs(valueN - value) ;
    }

    solution.setObjective(0, approximationToN);
  }
}
