package jmetal6.auto.component.selection.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import jmetal6.auto.component.selection.MatingPoolSelection;
import jmetal6.auto.util.preference.Preference;
import jmetal6.operator.selection.impl.NaryTournamentSelection;
import jmetal6.solution.Solution;
import jmetal6.util.comparator.MultiComparator;

public class NaryTournamentMatingPoolSelection<S extends Solution<?>>
    implements MatingPoolSelection<S> {
  private NaryTournamentSelection<S> selectionOperator;
  private int matingPoolSize;
  private Preference<S> preference ;

  @Deprecated
  public NaryTournamentMatingPoolSelection(
      int tournamentSize, int matingPoolSize, Comparator<S> comparator) {
    selectionOperator = new NaryTournamentSelection<>(tournamentSize, comparator);
    this.matingPoolSize = matingPoolSize;
  }

  public NaryTournamentMatingPoolSelection(
          int tournamentSize, int matingPoolSize, Preference<S> preference) {
    this.preference = preference ;
    Comparator<S> comparator = new MultiComparator<>(
            Arrays.asList(
                    preference.getRanking().getSolutionComparator(),
                    preference.getDensityEstimator().getSolutionComparator())) ;
    this.selectionOperator = new NaryTournamentSelection<>(tournamentSize, comparator) ;
    this.matingPoolSize = matingPoolSize ;
  }

  public List<S> select(List<S> solutionList) {
    preference.recompute(solutionList);
    List<S> matingPool = new ArrayList<>(matingPoolSize);

    while (matingPool.size() < matingPoolSize) {
      matingPool.add(selectionOperator.execute(solutionList));
    }

    return matingPool;
  }


}
