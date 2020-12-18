package jmetal6.operator.localsearch;

import jmetal6.operator.Operator;

/**
 * Interface representing a local search operator
 *
 * Created by cbarba on 5/3/15.
 */
public interface LocalSearchOperator <Source> extends Operator<Source, Source> {
  int getNumberOfImprovements() ;
  int getNumberOfEvaluations() ;
}
