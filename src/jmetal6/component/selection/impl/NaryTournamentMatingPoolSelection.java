package jmetal6.component.selection.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jmetal6.component.selection.MatingPoolSelection;
import jmetal6.operator.selection.impl.NaryTournamentSelection;
import jmetal6.solution.Solution;

public class NaryTournamentMatingPoolSelection<S extends Solution<?>> implements MatingPoolSelection<S> {
  private NaryTournamentSelection<S> selectionOperator;
  private int matingPoolSize;

  public NaryTournamentMatingPoolSelection(int tournamentSize, int matingPoolSize, Comparator<S> comparator) {
    selectionOperator =
        new NaryTournamentSelection<>(tournamentSize, comparator);
    this.matingPoolSize = matingPoolSize;
  }

  public List<S> select(List<S> solutionList) {
    List<S> matingPool = new ArrayList<>(matingPoolSize);

    while (matingPool.size() < matingPoolSize) {
      matingPool.add(selectionOperator.execute(solutionList));
    }

    return matingPool;
  }
}
