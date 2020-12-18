package jmetal6.example.multiobjective.nsgaii.jmetal5version;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.DNSGAII;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.comparator.RankingAndDirScoreDistanceComparator;
import jmetal6.util.fileinput.VectorFileUtils;

/**
 * created at 2:50 pm, 2019/1/29
 * runner of DIR-Enhanced NSGA-II
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class DNSGAIIRunner extends AbstractAlgorithmRunner {

  public static void main(String[] args) throws FileNotFoundException {

    String referenceParetoFront = "referenceFronts/DTLZ2.3D.pf";

    // problem
    String problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2";
    Problem<DoubleSolution> problem = ProblemUtils.loadProblem(problemName);

    // crossover
    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 30;
    CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    // mutation
    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    MutationOperator<DoubleSolution> mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    // selection
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<>(
            new RankingAndDirScoreDistanceComparator<>());

    int populationSize = 300;
    Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<>(problem, crossover, mutation, populationSize)
            .setMaxEvaluations(300000)
            .setVariant(NSGAIIBuilder.NSGAIIVariant.DNSGAII)
            .setSelectionOperator(selection).build();

    // reference vectors
    double[][] referenceVectors = VectorFileUtils.readVectors("MOEAD_Weights/W" + problem.getNumberOfObjectives() + "D_" + populationSize + ".dat");
    ((DNSGAII<DoubleSolution>) algorithm).setReferenceVectors(referenceVectors);

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);

    printQualityIndicators(population, referenceParetoFront);
  }
}
