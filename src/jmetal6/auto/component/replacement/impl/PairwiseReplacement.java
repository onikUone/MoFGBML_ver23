package jmetal6.auto.component.replacement.impl;

import java.util.ArrayList;
import java.util.List;

import jmetal6.auto.component.replacement.Replacement;
import jmetal6.operator.selection.impl.RankingAndCrowdingSelection;
import jmetal6.solution.Solution;
import jmetal6.util.checking.Check;
import jmetal6.util.comparator.DominanceComparator;

public class PairwiseReplacement<S extends Solution<?>> implements Replacement<S> {
  private DominanceComparator<S> dominanceComparator ;
  private Check checker = new Check();

  public PairwiseReplacement() {
    this(new DominanceComparator<>()) ;
  }
  /**
   * Constructor
   * @param dominanceComparator
   */
  public PairwiseReplacement(DominanceComparator<S> dominanceComparator) {
    this.dominanceComparator = dominanceComparator ;
  }

  public List<S> replace(List<S> population, List<S> offspringPopulation) {
    checker.that(population.size() == offspringPopulation.size(), "The population size (" +
            population.size()+ ") is not equal to the offspring population size (" + offspringPopulation.size()+")") ;

    List<S> temporaryPopulation = new ArrayList<>() ;
    for (int i = 0; i < population.size(); i++) {
      // Dominance test
      S child = offspringPopulation.get(i);
      int result;
      result = dominanceComparator.compare(population.get(i), child);
      if (result == -1) {
        // Solution i dominates child
        temporaryPopulation.add(population.get(i));
      } else if (result == 1) {
        // child dominates
        temporaryPopulation.add(child);
      } else {
        // the two population are non-dominated
        temporaryPopulation.add(child);
        temporaryPopulation.add(population.get(i));
      }
    }
    RankingAndCrowdingSelection<S> rankingAndCrowdingSelection;
    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(population.size(), dominanceComparator);

    List<S> newPopulation = new ArrayList<>(
            rankingAndCrowdingSelection.execute(temporaryPopulation));

    return newPopulation ;
  }
}
