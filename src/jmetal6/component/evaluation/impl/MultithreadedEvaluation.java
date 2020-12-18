package jmetal6.component.evaluation.impl;

import jmetal6.solution.Solution;
import jmetal6.util.evaluator.impl.MultithreadedSolutionListEvaluator;

public class MultithreadedEvaluation<S extends Solution<?>> extends AbstractEvaluation<S> {
  public MultithreadedEvaluation(int numberOfThreads) {
    super(new MultithreadedSolutionListEvaluator<S>(numberOfThreads)) ;
  }
}
