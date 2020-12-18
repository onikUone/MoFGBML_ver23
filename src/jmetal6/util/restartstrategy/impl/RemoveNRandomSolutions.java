package jmetal6.util.restartstrategy.impl;

import java.util.List;
import java.util.stream.IntStream;

import jmetal6.problem.DynamicProblem;
import jmetal6.solution.Solution;
import jmetal6.util.JMetalException;
import jmetal6.util.pseudorandom.JMetalRandom;
import jmetal6.util.restartstrategy.RemoveSolutionsStrategy;

/**
 * Created by antonio on 6/06/17.
 */
public class RemoveNRandomSolutions<S extends Solution<?>> implements RemoveSolutionsStrategy<S> {
  private int numberOfSolutionsToDelete ;

  public RemoveNRandomSolutions(int numberOfSolutionsToDelete) {
    this.numberOfSolutionsToDelete = numberOfSolutionsToDelete ;
  }

  @Override
  public int remove(List<S> solutionList, DynamicProblem<S, ?> problem) {
    if (solutionList == null) {
      throw new JMetalException("The solution list is null") ;
    } else if (problem == null) {
      throw new JMetalException("The problem is null") ;
    } else if (solutionList.size() == 0) {
      throw new JMetalException("The solution list is empty") ;
    }

    int numberOfSolutionsToRemove = numberOfSolutionsToDelete ;
    IntStream.range(0, numberOfSolutionsToRemove)
            .forEach(s -> {
                      int chosen = JMetalRandom.getInstance().nextInt(0, solutionList.size()-1);
                      solutionList.remove(chosen);
                    }
            );
    return numberOfSolutionsToDelete ;
  }
}
