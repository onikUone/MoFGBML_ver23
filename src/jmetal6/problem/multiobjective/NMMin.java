package jmetal6.problem.multiobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.problem.integerproblem.impl.AbstractIntegerProblem;
import jmetal6.solution.integersolution.IntegerSolution;
import jmetal6.solution.integersolution.impl.DefaultIntegerSolution;

/**
 * Created by Antonio J. Nebro on 03/07/14.
 * Bi-objective problem for testing class {@link DefaultIntegerSolution )}, e.g., integer encoding.
 * Objective 1: minimizing the distance to value N
 * Objective 2: minimizing the distance to value M
 */
@SuppressWarnings("serial")
public class NMMin extends AbstractIntegerProblem {
  private int valueN ;
  private int valueM ;

  public NMMin() {
    this(20, 100, -100, -1000, +1000);
  }

  /** Constructor */
  public NMMin(int numberOfVariables, int n, int m, int lowerBound, int upperBound)  {
    valueN = n ;
    valueM = m ;
    setNumberOfVariables(numberOfVariables);
    setNumberOfObjectives(2);
    setName("NMMin");

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
    int approximationToM ;

    approximationToN = 0;
    approximationToM = 0;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      int value = solution.getVariable(i) ;
      approximationToN += Math.abs(valueN - value) ;
      approximationToM += Math.abs(valueM - value) ;
    }

    solution.setObjective(0, approximationToN);
    solution.setObjective(1, approximationToM);
  }
}
