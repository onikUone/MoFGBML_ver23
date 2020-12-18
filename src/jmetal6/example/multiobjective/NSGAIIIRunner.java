package jmetal6.example.multiobjective;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
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
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/** Class to configure and run the NSGA-III algorithm */
public class NSGAIIIRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException Usage: three options -
   *     org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIIRunner -
   *     org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIIRunner problemName -
   *     org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIIRunner problemName paretoFrontFile
   */
  public static void main(String[] args) throws JMetalException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

    String problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";

    problem = ProblemUtils.loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 30.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    selection = new BinaryTournamentSelection<DoubleSolution>();

    algorithm =
        new NSGAIIIBuilder<>(problem)
            .setCrossoverOperator(crossover)
            .setMutationOperator(mutation)
            .setSelectionOperator(selection)
            .setMaxIterations(300)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.csv", ","))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.csv", ","))
        .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.csv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.csv");

//    PlotFront plot = new Plot3D(new ArrayFront(population).getMatrix());
//    plot.plot();
  }
}
