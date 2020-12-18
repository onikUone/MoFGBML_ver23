package jmetal6.example.multiobjective.nsgaii;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.multiobjective.nsgaii.NSGAII;
import jmetal6.component.termination.Termination;
import jmetal6.component.termination.impl.TerminationByEvaluations;
import jmetal6.lab.plot.PlotFront;
import jmetal6.lab.plot.impl.Plot2D;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * Class to configure and run the NSGA-II algorithm. At the end of the execution an HTML with the
 * produced front is shown.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIWithPlotliChartExample extends AbstractAlgorithmRunner {
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    NSGAII<DoubleSolution> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;

    String problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
    String referenceParetoFront = "referenceFronts/ZDT1.pf";

    problem = ProblemUtils.<DoubleSolution>loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    int populationSize = 100;
    int offspringPopulationSize = 100;

    Termination termination = new TerminationByEvaluations(25000);

    algorithm =
        new NSGAII<>(
            problem, populationSize, offspringPopulationSize, crossover, mutation, termination);

    algorithm.run();

    List<DoubleSolution> population = algorithm.getResult();
    JMetalLogger.logger.info("Total execution time : " + algorithm.getTotalComputingTime() + "ms");
    JMetalLogger.logger.info("Number of evaluations: " + algorithm.getEvaluations());

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.csv", ","))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.csv", ","))
        .print();

    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.csv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.csv");

    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }

    PlotFront plot = new Plot2D(new ArrayFront(population).getMatrix());
    plot.plot();
  }
}
