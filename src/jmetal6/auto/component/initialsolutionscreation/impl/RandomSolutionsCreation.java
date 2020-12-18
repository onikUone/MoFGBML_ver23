package jmetal6.auto.component.initialsolutionscreation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import jmetal6.auto.component.initialsolutionscreation.InitialSolutionsCreation;
import jmetal6.problem.Problem;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.zdt.ZDT4;
import jmetal6.solution.Solution;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AlgorithmDefaultOutputData;

public class RandomSolutionsCreation<S extends Solution<?>> implements InitialSolutionsCreation<S> {
  private final int numberOfSolutionsToCreate;
  private final Problem<S> problem;

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

  public static void main(String[] args) {
    DoubleProblem problem = new ZDT4(2);
    int numberOfSolutionsToCreate = 100;

    List<DoubleSolution> solutions =
        new RandomSolutionsCreation<>(problem, numberOfSolutionsToCreate).create();
    AlgorithmDefaultOutputData.generateMultiObjectiveAlgorithmOutputData(solutions, 0);
  }
}
