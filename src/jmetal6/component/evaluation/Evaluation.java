package jmetal6.component.evaluation;

import java.util.List;

import jmetal6.problem.Problem;
import jmetal6.solution.Solution;

public interface Evaluation<S extends Solution<?>> {
  List<S> evaluate(List<S> solutionList, Problem<S> problem) ;
  int getComputedEvaluations() ;
}
