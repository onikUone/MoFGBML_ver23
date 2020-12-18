package jmetal6.algorithm.multiobjective.mochc;

import java.util.List;

import jmetal6.algorithm.AlgorithmBuilder;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Builder class
 */
public class MOCHCBuilder implements AlgorithmBuilder<MOCHC> {
  BinaryProblem problem;
  SolutionListEvaluator<BinarySolution> evaluator;
  int populationSize;
  int maxEvaluations;
  int convergenceValue;
  double preservedPopulation;
  double initialConvergenceCount;
  CrossoverOperator<BinarySolution> crossoverOperator;
  MutationOperator<BinarySolution> cataclysmicMutation;
  SelectionOperator<List<BinarySolution>, BinarySolution> parentSelection;
  SelectionOperator<List<BinarySolution>, List<BinarySolution>> newGenerationSelection;

  public MOCHCBuilder(BinaryProblem problem) {
    this.problem = problem;
    evaluator = new SequentialSolutionListEvaluator<BinarySolution>() ;
    populationSize = 100 ;
    maxEvaluations = 25000 ;
    convergenceValue = 3 ;
    preservedPopulation = 0.05 ;
    initialConvergenceCount = 0.25 ;
  }

  /* Getters */
  public BinaryProblem getProblem() {
    return problem;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public int getMaxEvaluation() {
    return maxEvaluations;
  }

  public double getInitialConvergenceCount() {
    return initialConvergenceCount;
  }

  public int getConvergenceValue() {
    return convergenceValue;
  }

  public CrossoverOperator<BinarySolution> getCrossover() {
    return crossoverOperator;
  }

  public MutationOperator<BinarySolution> getCataclysmicMutation() {
    return cataclysmicMutation;
  }

  public SelectionOperator<List<BinarySolution>,BinarySolution> getParentSelection() {
    return parentSelection;
  }

  public SelectionOperator<List<BinarySolution>, List<BinarySolution>> getNewGenerationSelection() {
    return newGenerationSelection;
  }

  public double getPreservedPopulation() {
    return preservedPopulation;
  }

  /* Setters */
  public MOCHCBuilder setPopulationSize(int populationSize) {
    this.populationSize = populationSize;

    return this;
  }

  public MOCHCBuilder setMaxEvaluations(int maxEvaluations) {
    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public MOCHCBuilder setConvergenceValue(int convergenceValue) {
    this.convergenceValue = convergenceValue;

    return this;
  }

  public MOCHCBuilder setInitialConvergenceCount(double initialConvergenceCount) {
    this.initialConvergenceCount = initialConvergenceCount;

    return this;
  }

  public MOCHCBuilder setPreservedPopulation(double preservedPopulation) {
    this.preservedPopulation = preservedPopulation;

    return this;
  }

  public MOCHCBuilder setCrossover(CrossoverOperator<BinarySolution> crossover) {
    this.crossoverOperator = crossover;

    return this;
  }

  public MOCHCBuilder setCataclysmicMutation(MutationOperator<BinarySolution> cataclysmicMutation) {
    this.cataclysmicMutation = cataclysmicMutation;

    return this;
  }

  public MOCHCBuilder setParentSelection(SelectionOperator<List<BinarySolution>, BinarySolution> parentSelection) {
    this.parentSelection = parentSelection;

    return this;
  }

  public MOCHCBuilder setNewGenerationSelection(SelectionOperator<List<BinarySolution>, List<BinarySolution>> newGenerationSelection) {
    this.newGenerationSelection = newGenerationSelection;

    return this;
  }

  public MOCHCBuilder setEvaluator(SolutionListEvaluator<BinarySolution> evaluator) {
    this.evaluator = evaluator;

    return this;
  }

  public MOCHC build() {
    MOCHC algorithm =
        new MOCHC(problem, populationSize, maxEvaluations, convergenceValue, preservedPopulation,
            initialConvergenceCount, crossoverOperator, cataclysmicMutation, newGenerationSelection,
            parentSelection, evaluator);

    return algorithm;
  }
}
