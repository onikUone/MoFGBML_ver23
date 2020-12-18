package jmetal6.component.replacement.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jmetal6.component.replacement.Replacement;
import jmetal6.solution.Solution;
import jmetal6.util.comparator.DominanceComparator;
import jmetal6.util.comparator.ObjectiveComparator;

public class SingleObjectiveGenerationalReplacement<S extends Solution<?>> implements Replacement<S> {
  private DominanceComparator<S> dominanceComparator = new DominanceComparator<>() ;

  public List<S> replace(List<S> currentList, List<S> offspringList) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(currentList);
    jointPopulation.addAll(offspringList);

    Collections.sort(jointPopulation, new ObjectiveComparator<S>(0));

    while (jointPopulation.size() > currentList.size()) {
      jointPopulation.remove(jointPopulation.size() - 1);
    }

    return jointPopulation;
  }
}
