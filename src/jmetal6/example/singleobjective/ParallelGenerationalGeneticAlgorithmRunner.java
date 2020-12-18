package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SinglePointCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.problem.singleobjective.OneMax;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.evaluator.impl.MultithreadedSolutionListEvaluator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a parallel (multithreaded) generational genetic algorithm. The number
 * of cores is specified as an optional parameter. A default value is used is the parameter is not
 * provided. The target problem is OneMax
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ParallelGenerationalGeneticAlgorithmRunner {
  private static final int DEFAULT_NUMBER_OF_CORES = 0;
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.ParallelGenerationalGeneticAlgorithmRunner
   * [cores]
   */
  public static void main(String[] args) throws Exception {
    Algorithm<BinarySolution> algorithm;
    BinaryProblem problem = new OneMax(512);

    int numberOfCores;
    if (args.length == 1) {
      numberOfCores = Integer.valueOf(args[0]);
    } else {
      numberOfCores = DEFAULT_NUMBER_OF_CORES;
    }

    CrossoverOperator<BinarySolution> crossoverOperator = new SinglePointCrossover(0.9);
    MutationOperator<BinarySolution> mutationOperator =
        new BitFlipMutation(1.0 / problem.getBitsFromVariable(0));
    SelectionOperator<List<BinarySolution>, BinarySolution> selectionOperator =
        new BinaryTournamentSelection<BinarySolution>();

    GeneticAlgorithmBuilder<BinarySolution> builder =
        new GeneticAlgorithmBuilder<BinarySolution>(problem, crossoverOperator, mutationOperator)
            .setPopulationSize(100)
            .setMaxEvaluations(25000)
            .setSelectionOperator(selectionOperator)
            .setSolutionListEvaluator(
                new MultithreadedSolutionListEvaluator<BinarySolution>(numberOfCores));

    algorithm = builder.build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    builder.getEvaluator().shutdown();

    BinarySolution solution = algorithm.getResult();
    List<BinarySolution> population = new ArrayList<>(1);
    population.add(solution);

    long computingTime = algorithmRunner.getComputingTime();

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}
