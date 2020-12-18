package jmetal6.operator.mutation;

import jmetal6.operator.Operator;

/**
 * Interface representing mutation operators
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 *
 * @param <Source> The solution class of the solution to be mutated
 */
public interface MutationOperator<Source> extends Operator<Source, Source> {
  double getMutationProbability() ;
}
