package jmetal6.util.restartstrategy;

import java.util.List;

import jmetal6.problem.DynamicProblem;
import jmetal6.solution.Solution;

/**
 * Created by antonio on 6/06/17.
 */
public interface RestartStrategy<S extends Solution<?>> {
  void restart(List<S> solutionList, DynamicProblem<S,?> problem);
}
