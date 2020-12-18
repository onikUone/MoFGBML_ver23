package jmetal6.auto.component.selection;

import java.util.List;

import jmetal6.solution.Solution;

@FunctionalInterface
public interface MatingPoolSelection<S extends Solution<?>> {
  List<S> select(List<S> solutionList) ;
}
