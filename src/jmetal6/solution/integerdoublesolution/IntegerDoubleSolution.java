package jmetal6.solution.integerdoublesolution;

import jmetal6.solution.Solution;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.solution.integersolution.IntegerSolution;

/**
 * Interface representing solutions containing an integer solution and a double solution
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@Deprecated
public interface IntegerDoubleSolution extends Solution<Solution<?>> {
  IntegerSolution getIntegerSolution() ;
  DoubleSolution getDoubleSolution() ;
}
