package jmetal6.auto.component.variation.impl;

import java.util.ArrayList;
import java.util.List;

import jmetal6.auto.component.variation.Variation;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.NullMutation;
import jmetal6.solution.doublesolution.DoubleSolution;

/** */
public class DifferentialCrossoverVariation implements Variation<DoubleSolution> {
  private int matingPoolSize;
  private int offspringPopulationSize;

  private DifferentialEvolutionCrossover crossover;
  private MutationOperator<DoubleSolution> mutation;

  public DifferentialCrossoverVariation(
      int offspringPopulationSize,
      DifferentialEvolutionCrossover crossover,
      MutationOperator<DoubleSolution> mutation) {
    this.offspringPopulationSize = offspringPopulationSize;
    this.crossover = crossover;
    this.mutation = mutation;

    this.matingPoolSize = offspringPopulationSize * 3;
  }

  public DifferentialCrossoverVariation(
      int offspringPopulationSize, DifferentialEvolutionCrossover crossover) {
    this(offspringPopulationSize, crossover, new NullMutation<>());
  }

  @Override
  public List<DoubleSolution> variate(
      List<DoubleSolution> solutionList, List<DoubleSolution> matingPool) {

    List<DoubleSolution> offspringPopulation = new ArrayList<>();
    for (int i = 0; i < solutionList.size(); i++) {
      crossover.setCurrentSolution(solutionList.get(i));
      List<DoubleSolution> parents = new ArrayList<>(3);
      for (int j = 0; j < 3; j++) {
        parents.add(matingPool.get(0));
        matingPool.remove(0);
      }

      List<DoubleSolution> offspring = crossover.execute(parents);

      offspringPopulation.add(mutation.execute(offspring.get(0)));
    }
    return offspringPopulation;
  }

  @Override
  public int getMatingPoolSize() {
    return matingPoolSize;
  }

  @Override
  public int getOffspringPopulationSize() {
    return offspringPopulationSize;
  }
}
