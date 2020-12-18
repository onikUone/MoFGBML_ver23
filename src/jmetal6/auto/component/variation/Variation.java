package jmetal6.auto.component.variation;

import java.util.List;

import jmetal6.solution.Solution;

public interface Variation<S extends Solution<?>> {
  List<S> variate(List<S> solutionList, List<S> matingPool) ;

  int getMatingPoolSize() ;
  int getOffspringPopulationSize() ;
}
