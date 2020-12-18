package jmetal6.example.multiobjective.nsgaii.jmetal5version;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.IntegerDoubleSBXCrossover;
import jmetal6.operator.crossover.impl.IntegerSBXCrossover;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.NullMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.Problem;
import jmetal6.problem.multiobjective.NMMin2;
import jmetal6.solution.integerdoublesolution.IntegerDoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;

/**
 * Class to configure and run the NSGA-II algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIISolvingNMMin2Runner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<IntegerDoubleSolution> problem;
    Algorithm<List<IntegerDoubleSolution>> algorithm;
    CrossoverOperator<IntegerDoubleSolution> crossover;
    MutationOperator<IntegerDoubleSolution> mutation;
    SelectionOperator<List<IntegerDoubleSolution>, IntegerDoubleSolution> selection;

    problem = new NMMin2();

    crossover =
        new IntegerDoubleSBXCrossover(
            new IntegerSBXCrossover(0.9, 20), new SBXCrossover(0.9, 20.0));

    mutation = new NullMutation<>();

    selection =
        new BinaryTournamentSelection<IntegerDoubleSolution>(
            new RankingAndCrowdingDistanceComparator<IntegerDoubleSolution>());

    int populationSize = 100;
    algorithm =
        new NSGAIIBuilder<IntegerDoubleSolution>(problem, crossover, mutation, populationSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(25000)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<IntegerDoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    printFinalSolutionSet(population);
  }
}
