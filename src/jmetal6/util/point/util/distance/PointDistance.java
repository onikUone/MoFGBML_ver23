package jmetal6.util.point.util.distance;

import jmetal6.util.point.Point;

/**
 * Interface representing classes for computing a distance between two points
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface PointDistance {
  public double compute(Point pointA, Point pointB) ;
}
