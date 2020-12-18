package jmetal6.util.restartstrategy.impl;

import java.util.List;
import java.util.stream.IntStream;

import jmetal6.problem.DynamicProblem;
import jmetal6.solution.Solution;
import jmetal6.util.JMetalException;
import jmetal6.util.restartstrategy.CreateNewSolutionsStrategy;

/**
 * Created by antonio on 6/06/17.
 */
public class CreateNRandomSolutions<S extends Solution<?>> implements CreateNewSolutionsStrategy<S> {

  @Override
  public void create(List<S> solutionList, DynamicProblem<S, ?> problem, int numberOfSolutionsToCreate) {
    if (solutionList == null) {
      throw new JMetalException("The solution list is null") ;
    } else if (problem == null) {
      throw new JMetalException("The problem is null") ;
    }

    IntStream.range(0, numberOfSolutionsToCreate)
            .forEach(s -> {solutionList.add(problem.createSolution());});
  }
}
