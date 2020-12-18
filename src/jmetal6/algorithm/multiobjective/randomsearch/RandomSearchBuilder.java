package jmetal6.algorithm.multiobjective.randomsearch;

import jmetal6.algorithm.AlgorithmBuilder;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class RandomSearchBuilder<S extends Solution<?>> implements AlgorithmBuilder<RandomSearch<S>> {
  private Problem<S> problem ;
  private int maxEvaluations ;

  /* Getter */
  public int getMaxEvaluations() {
    return maxEvaluations;
  }


  public RandomSearchBuilder(Problem<S> problem) {
    this.problem = problem ;
    maxEvaluations = 25000 ;
  }

  public RandomSearchBuilder<S> setMaxEvaluations(int maxEvaluations) {
    this.maxEvaluations = maxEvaluations ;

    return this ;
  }

  public RandomSearch<S> build() {
    return new RandomSearch<S>(problem, maxEvaluations) ;
  }
} 
