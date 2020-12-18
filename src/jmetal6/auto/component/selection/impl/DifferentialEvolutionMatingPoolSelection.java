package jmetal6.auto.component.selection.impl;

import java.util.ArrayList;
import java.util.List;

import jmetal6.auto.component.selection.MatingPoolSelection;
import jmetal6.operator.selection.impl.DifferentialEvolutionSelection;
import jmetal6.solution.doublesolution.DoubleSolution;

public class DifferentialEvolutionMatingPoolSelection implements MatingPoolSelection<DoubleSolution> {
  private DifferentialEvolutionSelection selectionOperator ;
  private int matingPoolSize ;
  private int solutionCounter ;

  public DifferentialEvolutionMatingPoolSelection(int matingPoolSize) {
    selectionOperator = new DifferentialEvolutionSelection();
    this.matingPoolSize = matingPoolSize ;
    this.solutionCounter = 0 ;
  }

  public List<DoubleSolution> select(List<DoubleSolution> solutionList) {
    List<DoubleSolution> matingPool = new ArrayList<>(matingPoolSize) ;

    while (matingPool.size() < matingPoolSize) {
      selectionOperator.setIndex(solutionCounter);
      matingPool.addAll(selectionOperator.execute(solutionList)) ;
      solutionCounter++ ;
      if (solutionCounter % solutionList.size() == 0) {
        solutionCounter = 0 ;
      }
    }

    return matingPool ;
  }
}
