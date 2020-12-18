package jmetal6.util.restartstrategy.impl;

import static jmetal6.util.SolutionListUtils.*;

import java.util.List;

import jmetal6.problem.DynamicProblem;
import jmetal6.solution.Solution;
import jmetal6.util.JMetalException;
import jmetal6.util.restartstrategy.RemoveSolutionsStrategy;

/**
 * Created by antonio on 6/06/17.
 */
public class RemoveFirstNSolutions<S extends Solution<?>> implements RemoveSolutionsStrategy<S> {
  private int numberOfSolutionsToDelete ;

  public RemoveFirstNSolutions(int numberOfSolutionsToDelete) {
    this.numberOfSolutionsToDelete = numberOfSolutionsToDelete ;
  }

  @Override
  public int remove(List<S> solutionList, DynamicProblem<S, ?> problem) {
    if (solutionList == null) {
      throw new JMetalException("The solution list is null") ;
    } else if (problem == null) {
      throw new JMetalException("The problem is null") ;
    }

    removeSolutionsFromList(solutionList, numberOfSolutionsToDelete);
    return numberOfSolutionsToDelete ;
  }
}
