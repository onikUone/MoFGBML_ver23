package jmetal6.example.multiobjective.wasfga;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.wasfga.WASFGA;
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
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;

public class WASFGARunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.WASFGA.WASFGABinaryRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, IOException {
    Algorithm<List<PermutationSolution<Integer>>> algorithm;
    PermutationProblem<PermutationSolution<Integer>> problem;
    CrossoverOperator<PermutationSolution<Integer>> crossover;
    MutationOperator<PermutationSolution<Integer>> mutation;
    SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;

    crossover = new PMXCrossover(0.9);

    double mutationProbability = 0.2;
    mutation = new PermutationSwapMutation<Integer>(mutationProbability);

    selection =
        new BinaryTournamentSelection<PermutationSolution<Integer>>(
            new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());
    String referenceParetoFront = "";
    List<Double> referencePoint = null;

    // problem = ProblemUtils.<DoubleSolution> loadProblem(problemName);
    problem = new MultiobjectiveTSP("/tspInstances/kroA100.tsp", "/tspInstances/kroB100.tsp");

    referencePoint = new ArrayList<>();
    referencePoint.add(0.0);
    referencePoint.add(0.0);
    /*
    double crossoverProbability = 0.9 ;
    double crossoverDistributionIndex = 20.0 ;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());*/

    double epsilon = 0.01;
    algorithm =
        new WASFGA<PermutationSolution<Integer>>(
            problem,
            100,
            250,
            crossover,
            mutation,
            selection,
            new SequentialSolutionListEvaluator<PermutationSolution<Integer>>(),
            epsilon,
            referencePoint);

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<PermutationSolution<Integer>> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }
}
