package jmetal6.problem.permutationproblem.impl;

import jmetal6.problem.AbstractGenericProblem;
import jmetal6.problem.permutationproblem.PermutationProblem;
import jmetal6.solution.permutationsolution.PermutationSolution;
import jmetal6.solution.permutationsolution.impl.IntegerPermutationSolution;

@SuppressWarnings("serial")
public abstract class AbstractIntegerPermutationProblem
    extends AbstractGenericProblem<PermutationSolution<Integer>> implements
    PermutationProblem<PermutationSolution<Integer>> {

  @Override
  public PermutationSolution<Integer> createSolution() {
    return new IntegerPermutationSolution(getLength(), getNumberOfObjectives()) ;
  }
}
