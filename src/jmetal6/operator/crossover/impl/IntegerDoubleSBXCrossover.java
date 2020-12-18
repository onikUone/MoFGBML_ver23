package jmetal6.operator.crossover.impl;

import java.util.Arrays;
import java.util.List;

import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.solution.integerdoublesolution.IntegerDoubleSolution;
import jmetal6.solution.integerdoublesolution.impl.DefaultIntegerDoubleSolution;
import jmetal6.solution.integersolution.IntegerSolution;
import jmetal6.util.checking.Check;

/**
 * This class allows to apply a SBX crossover operator using two parent solutions of implementing
 * interface {@link IntegerDoubleSolution}. The class contains two instances of the SBX crossover,
 * one for integer and one for double solutions. Each operator is applied in the corresponding
 * solution to generate the children.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@Deprecated
public class IntegerDoubleSBXCrossover implements CrossoverOperator<IntegerDoubleSolution> {
  private IntegerSBXCrossover integerSBXCrossover;
  private SBXCrossover doubleSBXCrossover;

  /** Constructor */
  public IntegerDoubleSBXCrossover(
      IntegerSBXCrossover integerSBXCrossover, SBXCrossover doubleSBXCrossover) {
    this.integerSBXCrossover = integerSBXCrossover;
    this.doubleSBXCrossover = doubleSBXCrossover;
  }
  /** Execute() method */
  @Override
  public List<IntegerDoubleSolution> execute(List<IntegerDoubleSolution> parentSolutions) {
    Check.isNotNull(parentSolutions);
    Check.that(
        parentSolutions.size() == 2,
        "There must be two parents instead of " + parentSolutions.size());

    List<IntegerSolution> integerParentSolutions =
        Arrays.asList(
            parentSolutions.get(0).getIntegerSolution(),
            parentSolutions.get(1).getIntegerSolution());

    List<DoubleSolution> doubleParentSolutions =
        Arrays.asList(
            parentSolutions.get(0).getDoubleSolution(), parentSolutions.get(1).getDoubleSolution());

    List<IntegerSolution> integerChildrenSolutions =
        integerSBXCrossover.execute(integerParentSolutions);
    List<DoubleSolution> doubleChildrenSolutions =
        doubleSBXCrossover.execute(doubleParentSolutions);

    return Arrays.asList(
        new DefaultIntegerDoubleSolution(
            integerChildrenSolutions.get(0), doubleChildrenSolutions.get(0)),
        new DefaultIntegerDoubleSolution(
            integerChildrenSolutions.get(1), doubleChildrenSolutions.get(1)));
  }

  @Override
  public int getNumberOfRequiredParents() {
    return 2;
  }

  @Override
  public int getNumberOfGeneratedChildren() {
    return 2;
  }

  @Override
  public double getCrossoverProbability() {
    return 1.0 ;
  }
}
