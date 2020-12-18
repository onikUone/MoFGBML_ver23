package jmetal6.operator.crossover.impl;

import java.util.ArrayList;
import java.util.List;

import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.solution.Solution;
import jmetal6.util.checking.Check;

/**
 * This class defines a null crossover operator: the parent solutions are returned without any
 * change. It can be useful when configuring a genetic algorithm and we want to use only mutation.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings({ "unchecked", "serial" })
public class NullCrossover<S extends Solution<?>>
    implements CrossoverOperator<S> {

  /** Execute() method */
  @Override public List<S> execute(List<S> source) {
    Check.isNotNull(source);
    Check.that(source.size() == 2, "There must be two parents instead of " + source.size());

    List<S> list = new ArrayList<>() ;
    list.add((S) source.get(0).copy()) ;
    list.add((S) source.get(1).copy()) ;

    return list ;
  }

  public int getNumberOfRequiredParents() {
    return 2 ;
  }

  @Override
  public int getNumberOfGeneratedChildren() {
    return 2;
  }

  @Override
  public double getCrossoverProbability() {
    return 1.0;
  }
}
