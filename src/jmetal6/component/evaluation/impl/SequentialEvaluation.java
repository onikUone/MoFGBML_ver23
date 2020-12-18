package jmetal6.component.evaluation.impl;

import jmetal6.solution.Solution;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;

public class SequentialEvaluation<S extends Solution<?>> extends AbstractEvaluation<S> {
  public SequentialEvaluation() {
    super(new SequentialSolutionListEvaluator<S>()) ;
  }
}
