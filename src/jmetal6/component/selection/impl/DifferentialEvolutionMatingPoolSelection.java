package jmetal6.component.selection.impl;

import java.util.ArrayList;
import java.util.List;

import jmetal6.component.selection.MatingPoolSelection;
import jmetal6.operator.selection.impl.DifferentialEvolutionSelection;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.checking.Check;
import jmetal6.util.sequencegenerator.SequenceGenerator;

public class DifferentialEvolutionMatingPoolSelection
    implements MatingPoolSelection<DoubleSolution> {
  private DifferentialEvolutionSelection selectionOperator;
  private int matingPoolSize;
  private SequenceGenerator<Integer> solutionIndexGenerator ;

  public DifferentialEvolutionMatingPoolSelection(
      int matingPoolSize, int numberOfParentsToSelect, boolean takeCurrentIndividualAsParent, SequenceGenerator<Integer> solutionIndexGenerator) {
    selectionOperator = new DifferentialEvolutionSelection(numberOfParentsToSelect, takeCurrentIndividualAsParent);
    this.matingPoolSize = matingPoolSize;
    this.solutionIndexGenerator = solutionIndexGenerator ;
  }

  public List<DoubleSolution> select(List<DoubleSolution> solutionList) {
    List<DoubleSolution> matingPool = new ArrayList<>(matingPoolSize);

    while (matingPool.size() < matingPoolSize) {
      selectionOperator.setIndex(solutionIndexGenerator.getValue());
      List<DoubleSolution> parents = selectionOperator.execute(solutionList) ;
      for (DoubleSolution parent: parents)  {
        matingPool.add(parent);
        if (matingPool.size() == matingPoolSize) {
          break ;
        }
      }
    }

    Check.that(
        matingPoolSize == matingPool.size(),
        "The mating pool size "
            + matingPool.size()
            + " is not equal to the required size "
            + matingPoolSize);

    return matingPool;
  }
}
