package jmetal6.auto.util.fitness;

import java.util.List;

import jmetal6.solution.Solution;

public interface Fitness<S extends Solution<?>> {
  double getFitness(S solution) ;
  void computeFitness(List<S> solutionList) ;
}
