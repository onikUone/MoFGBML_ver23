package jmetal6.util.distance.impl;

import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.distance.Distance;

/**
 * Class for calculating the Euclidean distance between two {@link DoubleSolution} objects in solution space.
 *
 * @author <antonio@lcc.uma.es>
 */
public class EuclideanDistanceBetweenVectors implements Distance<double[], double[]> {

  @Override
  public double getDistance(double[] vector1, double[] vector2) {
    double distance = 0.0;

    double diff;
    for (int i = 0; i < vector1.length ; i++){
      diff = vector1[i] - vector2[i];
      distance += diff * diff ;
    }

    return Math.sqrt(distance);
  }
}
