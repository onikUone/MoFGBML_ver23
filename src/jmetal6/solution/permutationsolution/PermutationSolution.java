package jmetal6.solution.permutationsolution;

import jmetal6.solution.Solution;

/**
 * Interface representing permutation based solutions
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface PermutationSolution<T> extends Solution<T> {
  int getLength() ;
}
