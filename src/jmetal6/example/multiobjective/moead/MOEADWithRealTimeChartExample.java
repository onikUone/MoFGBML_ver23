package jmetal6.example.multiobjective.moead;

import java.io.FileNotFoundException;

import jmetal6.algorithm.multiobjective.moead.MOEAD;
import jmetal6.component.initialsolutioncreation.impl.RandomSolutionsCreation;
import jmetal6.component.replacement.impl.MOEADReplacement;
import jmetal6.component.selection.impl.PopulationAndNeighborhoodMatingPoolSelection;
import jmetal6.component.termination.impl.TerminationByEvaluations;
import jmetal6.component.variation.impl.DifferentialCrossoverVariation;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.ProblemUtils;
import jmetal6.util.aggregativefunction.AggregativeFunction;
import jmetal6.util.aggregativefunction.impl.Tschebyscheff;
import jmetal6.util.neighborhood.impl.WeightVectorNeighborhood;
import jmetal6.util.observer.impl.EvaluationObserver;
import jmetal6.util.observer.impl.RunTimeChartObserver;
import jmetal6.util.sequencegenerator.SequenceGenerator;
import jmetal6.util.sequencegenerator.impl.IntegerPermutationGenerator;

/**
 * Class for configuring and running the MOEA/D-DE algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class MOEADWithRealTimeChartExample extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.moead.MOEADRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws FileNotFoundException {
    DoubleProblem problem;
    MOEAD<DoubleSolution> algorithm;
    MutationOperator<DoubleSolution> mutation;
    DifferentialEvolutionCrossover crossover;

    String problemName = "org.uma.jmetal.problem.multiobjective.lz09.LZ09F2";
    String referenceParetoFront = "referenceFronts/LZ09_F2.pf";

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    int populationSize = 300;
    int offspringPopulationSize = 1;

    SequenceGenerator<Integer> subProblemIdGenerator = new IntegerPermutationGenerator(populationSize);

    double cr = 1.0;
    double f = 0.5;
    crossover =
        new DifferentialEvolutionCrossover(
            cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    DifferentialCrossoverVariation variation =
        new DifferentialCrossoverVariation(
            offspringPopulationSize, crossover, mutation, subProblemIdGenerator);

    double neighborhoodSelectionProbability = 0.9;
    int neighborhoodSize = 20;
    WeightVectorNeighborhood<DoubleSolution> neighborhood =
        new WeightVectorNeighborhood<>(populationSize, neighborhoodSize);

    PopulationAndNeighborhoodMatingPoolSelection<DoubleSolution> selection =
        new PopulationAndNeighborhoodMatingPoolSelection<>(
            variation.getCrossover().getNumberOfRequiredParents(),
            subProblemIdGenerator,
            neighborhood,
            neighborhoodSelectionProbability,
            true);

    int maximumNumberOfReplacedSolutions = 2;
    AggregativeFunction aggregativeFunction = new Tschebyscheff();
    MOEADReplacement replacement =
        new MOEADReplacement(
            selection,
            neighborhood,
            aggregativeFunction,
            subProblemIdGenerator,
            maximumNumberOfReplacedSolutions);

    algorithm =
        new MOEAD<>(
            problem,
            populationSize,
            new RandomSolutionsCreation<>(problem, populationSize),
            variation,
            selection,
            replacement,
            new TerminationByEvaluations(150000));

    EvaluationObserver evaluationObserver = new EvaluationObserver(1000);
    RunTimeChartObserver<DoubleSolution> runTimeChartObserver =
        new RunTimeChartObserver<>("NSGA-II", 80, 1000, referenceParetoFront);

    algorithm.getObservable().register(evaluationObserver);
    algorithm.getObservable().register(runTimeChartObserver);

    algorithm.run();

    System.exit(0);
  }
}
