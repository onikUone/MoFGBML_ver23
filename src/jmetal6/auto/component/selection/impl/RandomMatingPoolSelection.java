package jmetal6.auto.component.selection.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import jmetal6.auto.component.selection.MatingPoolSelection;
import jmetal6.solution.Solution;
import jmetal6.util.checking.Check;
import jmetal6.util.pseudorandom.JMetalRandom;

public class RandomMatingPoolSelection<S extends Solution<?>> implements MatingPoolSelection<S> {
  private int matingPoolSize;

  public RandomMatingPoolSelection(int matingPoolSize) {
    this.matingPoolSize = matingPoolSize;
  }

  public List<S> select(List<S> solutionList) {
    Check.isNotNull(solutionList);

    List<S> matingPool = new ArrayList<>();
    IntStream.range(0, matingPoolSize)
        .forEach(
            i ->
                matingPool.add(
                    solutionList.get(JMetalRandom.getInstance().nextInt(0, solutionList.size()-1))));

    return matingPool;
  }
}
