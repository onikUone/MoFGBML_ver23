package jmetal6.problem.binaryproblem;

import java.util.List;

import jmetal6.problem.Problem;
import jmetal6.solution.binarysolution.BinarySolution;

/**
 * Interface representing binary problems
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface BinaryProblem extends Problem<BinarySolution> {
  List<Integer> getListOfBitsPerVariable() ;
  int getBitsFromVariable(int index) ;
  int getTotalNumberOfBits() ;
}
