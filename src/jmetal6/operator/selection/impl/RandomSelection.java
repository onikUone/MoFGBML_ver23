package jmetal6.operator.selection.impl;

import java.util.List;

import jmetal6.operator.selection.SelectionOperator;
import jmetal6.util.SolutionListUtils;
import jmetal6.util.checking.Check;

/**
 * This class implements a random selection operator used for selecting randomly a solution from a list
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class RandomSelection<S> implements SelectionOperator<List<S>, S> {

  /** Execute() method */
  public S execute(List<S> solutionList) {
    Check.isNotNull(solutionList);
    Check.collectionIsNotEmpty(solutionList);

    List<S> list = SolutionListUtils.selectNRandomDifferentSolutions(1, solutionList);

    return list.get(0) ;
  }
}
