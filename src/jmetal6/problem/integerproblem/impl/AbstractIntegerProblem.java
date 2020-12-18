package jmetal6.problem.integerproblem.impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import jmetal6.problem.AbstractGenericProblem;
import jmetal6.problem.integerproblem.IntegerProblem;
import jmetal6.solution.integersolution.IntegerSolution;
import jmetal6.solution.integersolution.impl.DefaultIntegerSolution;
import jmetal6.util.checking.Check;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("serial")
public abstract class AbstractIntegerProblem extends AbstractGenericProblem<IntegerSolution>
  implements IntegerProblem {

  protected List<Pair<Integer, Integer>> bounds;

  public List<Pair<Integer, Integer>> getVariableBounds() {
    return bounds;
  }

  @Override
  public Integer getUpperBound(int index) {
    return getVariableBounds().get(index).getRight();
  }

  @Override
  public Integer getLowerBound(int index) {
    return getVariableBounds().get(index).getLeft();
  }

  public void setVariableBounds(List<Integer> lowerBounds, List<Integer> upperBounds) {
    Check.isNotNull(lowerBounds);
    Check.isNotNull(upperBounds);
    Check.that(
            lowerBounds.size() == upperBounds.size(),
            "The size of the lower bound list is not equal to the size of the upper bound list");

    bounds =
        IntStream.range(0, lowerBounds.size())
            .mapToObj(i -> new ImmutablePair<>(lowerBounds.get(i), upperBounds.get(i)))
            .collect(Collectors.toList());
  }

  @Override
  public IntegerSolution createSolution() {
    return new DefaultIntegerSolution(getVariableBounds(), getNumberOfObjectives());
  }

  @Override
  public List<Pair<Integer, Integer>> getBounds() {
    return bounds ;
  }
}
