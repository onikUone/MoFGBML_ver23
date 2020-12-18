package jmetal6.algorithm.impl;

import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.selection.impl.DifferentialEvolutionSelection;
import jmetal6.solution.doublesolution.DoubleSolution;

/**
 * Abstract class representing differential evolution (DE) algorithms
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractDifferentialEvolution<Result> extends AbstractEvolutionaryAlgorithm<DoubleSolution, Result> {
  protected DifferentialEvolutionCrossover crossoverOperator ;
  protected DifferentialEvolutionSelection selectionOperator ;
}
