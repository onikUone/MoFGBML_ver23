package jmetal6.component.initialsolutioncreation;

import java.util.List;

import jmetal6.solution.Solution;

/**
 * Interface representing entities that create a list of solutions applying some strategy (e.g, random)
 *
 * @param <S>
 */
@FunctionalInterface
public interface InitialSolutionsCreation<S extends Solution<?>> {
  List<S> create() ;
}
