package jmetal6.component.evaluation.impl;

import java.util.List;

import jmetal6.component.evaluation.Evaluation;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.evaluator.SolutionListEvaluator;

public abstract  class AbstractEvaluation<S extends Solution<?>> implements Evaluation<S> {
  private SolutionListEvaluator<S> evaluator ;
  private int numberOfComputedEvaluations ;

  public AbstractEvaluation(SolutionListEvaluator<S> evaluator) {
    this.numberOfComputedEvaluations = 0 ;
    this.evaluator = evaluator ;
  }

  @Override
  public List<S> evaluate(List<S> solutionList, Problem<S> problem) {
    evaluator.evaluate(solutionList, problem) ;

    numberOfComputedEvaluations += solutionList.size() ;

    return solutionList;
  }

  public int getComputedEvaluations() {
    return numberOfComputedEvaluations ;
  }
}
