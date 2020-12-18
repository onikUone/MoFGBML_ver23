package jmetal6.solution.binarysolution;

import jmetal6.solution.Solution;
import jmetal6.util.binarySet.BinarySet;

/**
 * Interface representing a binary (bitset) solutions
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface BinarySolution extends Solution<BinarySet> {
  int getNumberOfBits(int index) ;
  int getTotalNumberOfBits() ;
}
