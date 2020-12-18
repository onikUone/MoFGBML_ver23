package jmetal6.algorithm.impl;

import jmetal6.operator.mutation.MutationOperator;
import jmetal6.problem.Problem;
/**
 * Abstract class representing an evolution strategy algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

@SuppressWarnings("serial")
public abstract class AbstractEvolutionStrategy<S, Result> extends AbstractEvolutionaryAlgorithm<S, Result> {
  protected MutationOperator<S> mutationOperator ;

  /* Getter */
  public MutationOperator<S> getMutationOperator() {
    return mutationOperator;
  }

  /**
   * Constructor
   * @param problem The problem to solve
   */
  public AbstractEvolutionStrategy(Problem<S> problem) {
    setProblem(problem);
  }
}
