package jmetal6.auto.component.evaluation;

import java.util.List;

import jmetal6.solution.Solution;
import jmetal6.util.observable.ObservableEntity;

public interface Evaluation<S extends Solution<?>> extends ObservableEntity {
  List<S> evaluate(List<S> solutionList) ;
}
