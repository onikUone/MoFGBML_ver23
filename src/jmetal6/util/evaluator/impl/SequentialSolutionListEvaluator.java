package jmetal6.util.evaluator.impl;

import java.util.List;

import jmetal6.problem.Problem;
import jmetal6.util.JMetalException;
import jmetal6.util.evaluator.SolutionListEvaluator;

/**
 * @author Antonio J. Nebro
 */
@SuppressWarnings("serial")
public class SequentialSolutionListEvaluator<S> implements SolutionListEvaluator<S> {

  @Override
  public List<S> evaluate(List<S> solutionList, Problem<S> problem) throws JMetalException {
    solutionList.stream().forEach(s -> problem.evaluate(s));

    return solutionList;
  }

  @Override
  public void shutdown() {
    // This method is an intentionally-blank override.
  }
}
