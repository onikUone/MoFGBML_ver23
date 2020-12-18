package jmetal6.util.distance.impl;

import jmetal6.solution.Solution;
import jmetal6.util.distance.Distance;

/**
 * Class for calculating the Euclidean distance between two {@link Solution} objects in objective space.
 *
 * @author <antonio@lcc.uma.es>
 */
public class EuclideanDistanceBetweenSolutionsInObjectiveSpace<S extends Solution<?>>
    implements Distance<S, S> {

  private EuclideanDistanceBetweenVectors distance = new EuclideanDistanceBetweenVectors() ;

  @Override
  public double getDistance(S solution1, S solution2) {
    return distance.getDistance(solution1.getObjectives(), solution2.getObjectives()) ;
  }
}
