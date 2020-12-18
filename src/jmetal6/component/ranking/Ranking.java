package jmetal6.component.ranking;

import java.util.List;

import jmetal6.solution.util.attribute.Attribute;

/**
 * Ranks a list of population according to the dominance relationship
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface Ranking<S> extends Attribute<S> {
  Ranking<S> computeRanking(List<S> solutionList) ;
  List<S> getSubFront(int rank) ;
  int getNumberOfSubFronts() ;
}
