package jmetal6.problem.multiobjective;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import jmetal6.problem.binaryproblem.impl.AbstractBinaryProblem;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.solution.binarysolution.impl.DefaultBinarySolution;
import jmetal6.util.JMetalException;

/**
 * Class representing problem OneZeroMax. The problem consist of maximizing the
 * number of '1's and '0's in a binary string.
 */
@SuppressWarnings("serial")
public class OneZeroMax extends AbstractBinaryProblem {
  private int bits ;
  /** Constructor */
  public OneZeroMax() throws JMetalException {
    this(512);
  }

  /** Constructor */
  public OneZeroMax(Integer numberOfBits) throws JMetalException {
    setNumberOfVariables(1);
    setNumberOfObjectives(2);
    setName("OneZeroMax");

    bits = numberOfBits ;
  }

  @Override
  public List<Integer> getListOfBitsPerVariable() {
    return Arrays.asList(bits);
  }

  @Override
  public int getBitsFromVariable(int index) {
  	if (index != 0) {
  		throw new JMetalException("Problem OneZeroMax has only a variable. Index = " + index) ;
  	}
  	return bits ;
  }

  @Override
  public BinarySolution createSolution() {
    return new DefaultBinarySolution(getListOfBitsPerVariable(), getNumberOfObjectives()) ;
  }

  /** Evaluate() method */
  @Override
    public void evaluate(BinarySolution solution) {
    int counterOnes;
    int counterZeroes;

    counterOnes = 0;
    counterZeroes = 0;

    BitSet bitset = solution.getVariable(0) ;

    for (int i = 0; i < bitset.length(); i++) {
      if (bitset.get(i)) {
        counterOnes++;
      } else {
        counterZeroes++;
      }
    }

    // OneZeroMax is a maximization problem: multiply by -1 to minimize
    solution.setObjective(0, -1.0 * counterOnes);
    solution.setObjective(1, -1.0 * counterZeroes);
  }
}
