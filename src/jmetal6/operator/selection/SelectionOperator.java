package jmetal6.operator.selection;

import jmetal6.operator.Operator;

/**
 * Interface representing selection operators
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @param <Source> Class of the source object (typically, a list of solutions)
 * @param <Result> Class of the result of applying the operator
 */
public interface SelectionOperator<Source, Result> extends Operator<Source,Result> {
}
