package jmetal6.example.multiobjective.nsgaii.jmetal5version;

import java.io.IOException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.PMXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PermutationSwapMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.multiobjective.MultiobjectiveTSP;
import jmetal6.problem.permutationproblem.PermutationProblem;
import jmetal6.solution.permutationsolution.PermutationSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * Class for configuring and running the NSGA-II algorithm to solve the bi-objective TSP
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIITSPRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIITSPRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, IOException {
    JMetalRandom.getInstance().setSeed(100L);

    PermutationProblem<PermutationSolution<Integer>> problem;
    Algorithm<List<PermutationSolution<Integer>>> algorithm;
    CrossoverOperator<PermutationSolution<Integer>> crossover;
    MutationOperator<PermutationSolution<Integer>> mutation;
    SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;

    problem = new MultiobjectiveTSP("/tspInstances/kroA100.tsp", "/tspInstances/kroB100.tsp");

    crossover = new PMXCrossover(0.9);

    double mutationProbability = 0.2;
    mutation = new PermutationSwapMutation<Integer>(mutationProbability);

    selection =
        new BinaryTournamentSelection<PermutationSolution<Integer>>(
            new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

    int populationSize = 100;
    algorithm =
        new NSGAIIBuilder<PermutationSolution<Integer>>(
                problem, crossover, mutation, populationSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(10000)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<PermutationSolution<Integer>> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}
