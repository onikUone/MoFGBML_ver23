package jmetal6.auto.component.initialsolutionscreation;

import java.util.List;

import jmetal6.solution.Solution;

@FunctionalInterface
public interface InitialSolutionsCreation<S extends Solution<?>> {
  List<S> create() ;
}
