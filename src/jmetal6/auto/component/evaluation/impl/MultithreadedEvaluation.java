package jmetal6.auto.component.evaluation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal6.auto.component.evaluation.Evaluation;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.observable.Observable;
import jmetal6.util.observable.impl.DefaultObservable;

public class MultithreadedEvaluation<S extends Solution<?>> implements Evaluation<S> {
  private Problem<S> problem;
  protected Observable<Map<String, Object>> observable;

  public MultithreadedEvaluation(Problem<S> problem) {
    this.problem = problem;
    this.observable = new DefaultObservable<>("MultithreadedEvaluation") ;
  }

  public List<S> evaluate(List<S> solutionList) {
    solutionList.parallelStream().forEach(solution -> problem.evaluate(solution));

    Map<String, Object>attributes = new HashMap<>() ;
    attributes.put("POPULATION", solutionList);
    observable.setChanged();
    observable.notifyObservers(attributes);

    return solutionList;
  }

  @Override
  public Observable<Map<String, Object>> getObservable() {
    return observable;
  }
}
