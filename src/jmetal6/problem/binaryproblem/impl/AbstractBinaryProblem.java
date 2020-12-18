package jmetal6.problem.binaryproblem.impl;

import java.util.List;

import jmetal6.problem.AbstractGenericProblem;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.solution.binarysolution.impl.DefaultBinarySolution;

@SuppressWarnings("serial")
public abstract class AbstractBinaryProblem extends AbstractGenericProblem<BinarySolution>
  implements BinaryProblem {

  public abstract List<Integer> getListOfBitsPerVariable() ;

  @Override
  public int getBitsFromVariable(int index) {
    return getListOfBitsPerVariable().get(index) ;
  }
  
  @Override
  public int getTotalNumberOfBits() {
  	int count = 0 ;
  	for (int i = 0; i < this.getNumberOfVariables(); i++) {
  		count += this.getListOfBitsPerVariable().get(i) ;
  	}
  	
  	return count ;
  }

  @Override
  public BinarySolution createSolution() {
    return new DefaultBinarySolution(getListOfBitsPerVariable(), getNumberOfObjectives())  ;
  }
}
