package jmetal6.component.initialsolutioncreation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import jmetal6.component.initialsolutioncreation.InitialSolutionsCreation;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;

/**
 * Class that creates a list of randomly instantiated solutions
 *
 * @param <S>
 */
public class RandomSolutionsCreation<S extends Solution<?>> implements InitialSolutionsCreation<S> {
  private final int numberOfSolutionsToCreate;
  private final Problem<S> problem;

  /**
   * Creates the list of solutions
   * @param problem Problem defining the solutions
   * @param numberOfSolutionsToCreate
   */
  public RandomSolutionsCreation(Problem<S> problem, int numberOfSolutionsToCreate) {
    this.problem = problem;
    this.numberOfSolutionsToCreate = numberOfSolutionsToCreate;
  }

  public List<S> create() {
    List<S> solutionList = new ArrayList<>(numberOfSolutionsToCreate);
    IntStream.range(0, numberOfSolutionsToCreate)
        .forEach(i -> solutionList.add(problem.createSolution()));

    return solutionList;
  }
}
