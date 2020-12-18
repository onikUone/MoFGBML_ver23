package jmetal6.algorithm;

import java.util.Map;

import jmetal6.problem.DynamicProblem;
import jmetal6.util.observable.Observable;
import jmetal6.util.restartstrategy.RestartStrategy;

/**
 * @author Antonio J. Nebro <ajnebro@uma.es>
 */
public interface DynamicAlgorithm<Result> extends Algorithm<Result> {
  DynamicProblem<?, ?> getDynamicProblem() ;

  void restart();
  RestartStrategy<?> getRestartStrategy();
  Observable<Map<String, Object>> getObservable() ;
}
