package jmetal6.example.multiobjective.nsgaii;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.multiobjective.nsgaii.NSGAII;
import jmetal6.component.termination.Termination;
import jmetal6.component.termination.impl.TerminationByEvaluations;
import jmetal6.lab.plot.PlotFront;
import jmetal6.lab.plot.impl.Plot2DSmile;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SinglePointCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.problem.multiobjective.OneZeroMax;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * Class to configure and run the NSGA-II algorithm to solve a binary problem
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIBinaryProblemExample extends AbstractAlgorithmRunner {
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    BinaryProblem problem;
    NSGAII<BinarySolution> algorithm;
    CrossoverOperator<BinarySolution> crossover;
    MutationOperator<BinarySolution> mutation;

    problem = new OneZeroMax(250);

    crossover = new SinglePointCrossover(0.9);

    mutation = new BitFlipMutation(1.0 / problem.getTotalNumberOfBits());

    int populationSize = 100;
    int offspringPopulationSize = 100;

    Termination termination = new TerminationByEvaluations(25000);

    algorithm =
        new NSGAII<>(
            problem, populationSize, offspringPopulationSize, crossover, mutation, termination);

    algorithm.run();

    List<BinarySolution> population = algorithm.getResult();

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.csv", ","))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.csv", ","))
        .print();

    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.csv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.csv");

    PlotFront plot = new Plot2DSmile(new ArrayFront(population).getMatrix());
    plot.plot();
  }
}
