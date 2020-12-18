package jmetal6.algorithm.multiobjective.moead;

import java.util.HashMap;

import jmetal6.component.evaluation.impl.SequentialEvaluation;
import jmetal6.component.initialsolutioncreation.InitialSolutionsCreation;
import jmetal6.component.initialsolutioncreation.impl.RandomSolutionsCreation;
import jmetal6.component.replacement.impl.MOEADReplacement;
import jmetal6.component.selection.impl.PopulationAndNeighborhoodMatingPoolSelection;
import jmetal6.component.termination.Termination;
import jmetal6.component.termination.impl.TerminationByEvaluations;
import jmetal6.component.variation.impl.DifferentialCrossoverVariation;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.aggregativefunction.AggregativeFunction;
import jmetal6.util.neighborhood.impl.WeightVectorNeighborhood;
import jmetal6.util.observable.impl.DefaultObservable;
import jmetal6.util.sequencegenerator.SequenceGenerator;
import jmetal6.util.sequencegenerator.impl.IntegerPermutationGenerator;

/**
 * This class is intended to provide an implementation of the MOEA/D-DE algorithm including a constructor with the
 * typical parameters.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es> */
public class MOEADDE extends MOEAD<DoubleSolution> {

  /** Constructor */
  public MOEADDE(
      Problem<DoubleSolution> problem,
      int populationSize,
      InitialSolutionsCreation<DoubleSolution> initialSolutionsCreation,
      DifferentialCrossoverVariation variation,
      PopulationAndNeighborhoodMatingPoolSelection<DoubleSolution> selection,
      MOEADReplacement replacement,
      Termination termination) {
    super(
        problem,
        populationSize,
        initialSolutionsCreation,
        variation,
        selection,
        replacement,
        termination);
  }

  /**
   * Constructor with the parameters used in the paper describing MOEA/D-DE.
   *
   * @param problem
   * @param populationSize
   * @param maxNumberOfEvaluations
   * @param f
   * @param cr
   * @param neighborhoodSelectionProbability
   * @param maximumNumberOfReplacedSolutions
   * @param neighborhoodSize
   */
  public MOEADDE(Problem<DoubleSolution> problem,
                 int populationSize,
                 int maxNumberOfEvaluations,
                 double cr,
                 double f,
                 AggregativeFunction aggregativeFunction,
                 double neighborhoodSelectionProbability,
                 int maximumNumberOfReplacedSolutions,
                 int neighborhoodSize) {
    this.problem = problem ;
    this.populationSize = populationSize ;

    this.offspringPopulationSize = 1 ;

    SequenceGenerator<Integer> subProblemIdGenerator =
            new IntegerPermutationGenerator(populationSize);

    this.initialSolutionsCreation = new RandomSolutionsCreation<>(problem, populationSize) ;

    DifferentialEvolutionCrossover crossover =
            new DifferentialEvolutionCrossover(
                    cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    PolynomialMutation mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    this.variation =
            new DifferentialCrossoverVariation(
                    offspringPopulationSize, crossover, mutation, subProblemIdGenerator);

    WeightVectorNeighborhood<DoubleSolution> neighborhood =
            new WeightVectorNeighborhood<>(
                    populationSize,
                    neighborhoodSize);

    this.selection =
            new PopulationAndNeighborhoodMatingPoolSelection<>(
                    ((DifferentialCrossoverVariation)variation).getCrossover().getNumberOfRequiredParents(),
                    subProblemIdGenerator,
                    neighborhood,
                    neighborhoodSelectionProbability,
                    true);

    this.replacement =
            new MOEADReplacement(
                    (PopulationAndNeighborhoodMatingPoolSelection)selection,
                    neighborhood,
                    aggregativeFunction,
                    subProblemIdGenerator,
                    maximumNumberOfReplacedSolutions);

    this.termination = new TerminationByEvaluations(maxNumberOfEvaluations) ;

    this.evaluation = new SequentialEvaluation<>();

    this.algorithmStatusData = new HashMap<>();
    this.observable = new DefaultObservable<>("MOEA/D algorithm");
  }
}
