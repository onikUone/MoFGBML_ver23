package jmetal6.problem.permutationproblem;

import jmetal6.problem.Problem;
import jmetal6.solution.permutationsolution.PermutationSolution;

/**
 * Interface representing permutation problems
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface PermutationProblem<S extends PermutationSolution<?>> extends Problem<S> {
  int getLength() ;
}
