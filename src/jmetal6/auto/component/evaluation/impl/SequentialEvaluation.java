package jmetal6.auto.component.evaluation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal6.auto.component.evaluation.Evaluation;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.observable.Observable;
import jmetal6.util.observable.impl.DefaultObservable;

public class SequentialEvaluation<S extends Solution<?>> implements Evaluation<S> {
  private Problem<S> problem;
  private Observable<Map<String, Object>> observable;
  private int numberOfEvaluationIterations ;


  public SequentialEvaluation(Problem<S> problem) {
    this.problem = problem;
    this.observable = new DefaultObservable<>("SequentialEvaluation") ;
    this.numberOfEvaluationIterations = 0 ;
  }

  public List<S> evaluate(List<S> solutionList) {
    solutionList.forEach(solution -> problem.evaluate(solution));

    Map<String, Object>attributes = new HashMap<>() ;
    attributes.put("POPULATION", solutionList);
    attributes.put("EVALUATIONS", ++numberOfEvaluationIterations);
    observable.setChanged();
    observable.notifyObservers(attributes);

    return solutionList;
  }

  @Override
  public Observable<Map<String, Object>> getObservable() {
    return observable;
  }
}
