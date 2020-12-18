package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.NullCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.CharSequenceRandomMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.singleobjective.StringMatching;
import jmetal6.solution.sequencesolution.impl.CharSequenceSolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a generational genetic algorithm. The target problem is {@link StringMatching}.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class GenerationalGeneticAlgorithmStringMatchingRunner {
  public static void main(String[] args) {
    StringMatching problem;
    Algorithm<CharSequenceSolution> algorithm;
    CrossoverOperator<CharSequenceSolution> crossover;
    MutationOperator<CharSequenceSolution> mutation;
    SelectionOperator<List<CharSequenceSolution>, CharSequenceSolution> selection;

    problem = new StringMatching("jMetal is an optimization framework");

    crossover = new NullCrossover<>();

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    mutation = new CharSequenceRandomMutation(mutationProbability, problem.getAlphabet());

    selection = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());

    algorithm =
        new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
            .setPopulationSize(50)
            .setMaxEvaluations(250000)
            .setSelectionOperator(selection)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    CharSequenceSolution solution = algorithm.getResult();
    List<CharSequenceSolution> population = new ArrayList<>(1);
    population.add(solution);

    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info(
        "Best found string: '"
            + solution.getVariables().stream().map(String::valueOf).collect(Collectors.joining())
            + "'");

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}
