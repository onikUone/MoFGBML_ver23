package jmetal6.util.restartstrategy.impl;

import java.util.List;

import jmetal6.problem.DynamicProblem;
import jmetal6.solution.Solution;
import jmetal6.util.JMetalException;
import jmetal6.util.archive.impl.CrowdingDistanceArchive;
import jmetal6.util.restartstrategy.RemoveSolutionsStrategy;

/**
 * Created by antonio on 6/06/17.
 */
public class RemoveNSolutionsAccordingToTheCrowdingDistance<S extends Solution<?>> implements RemoveSolutionsStrategy<S> {
  private int numberOfSolutionsToDelete ;

  public RemoveNSolutionsAccordingToTheCrowdingDistance(int numberOfSolutionsToDelete) {
    this.numberOfSolutionsToDelete = numberOfSolutionsToDelete ;
  }

  @Override
  public int remove(List<S> solutionList, DynamicProblem<S, ?> problem) {
    if (solutionList == null) {
      throw new JMetalException("The solution list is null") ;
    } else if (problem == null) {
      throw new JMetalException("The problem is null") ;
    }
    int numberOfSolutions = solutionList.size() - numberOfSolutionsToDelete;
    if(numberOfSolutions < 0){
      numberOfSolutions = solutionList.size();
    }
    CrowdingDistanceArchive<S> archive = new CrowdingDistanceArchive<>(numberOfSolutions) ;
    for (S solution: solutionList) {
      archive.add(solution) ;
    }
    solutionList.clear();

    for (S solution: archive.getSolutionList()) {
      solutionList.add(solution) ;
    }

    return numberOfSolutions ;
  }
}
