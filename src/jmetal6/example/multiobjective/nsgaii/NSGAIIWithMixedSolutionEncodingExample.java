package jmetal6.example.multiobjective.nsgaii;

import java.io.FileNotFoundException;
import java.util.Arrays;

import jmetal6.algorithm.multiobjective.nsgaii.NSGAII;
import jmetal6.component.termination.Termination;
import jmetal6.component.termination.impl.TerminationByEvaluations;
import jmetal6.operator.crossover.impl.CompositeCrossover;
import jmetal6.operator.crossover.impl.IntegerSBXCrossover;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.impl.CompositeMutation;
import jmetal6.operator.mutation.impl.IntegerPolynomialMutation;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.Problem;
import jmetal6.problem.multiobjective.MixedIntegerDoubleProblem;
import jmetal6.solution.compositesolution.CompositeSolution;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.observer.impl.EvaluationObserver;
import jmetal6.util.observer.impl.RunTimeChartObserver;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * Class to configure and run the NSGA-II algorithm. A chart showing the front at the end of each
 * iteration is displayed.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIWithMixedSolutionEncodingExample extends AbstractAlgorithmRunner {
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<CompositeSolution> problem = new MixedIntegerDoubleProblem(10, 10, 100, -100, -1000, +1000);

    CompositeCrossover crossover =
        new CompositeCrossover(
            Arrays.asList(new IntegerSBXCrossover(1.0, 20.0), new SBXCrossover(1.0, 20.0)));

    CompositeMutation mutation =
        new CompositeMutation(
            Arrays.asList(
                new IntegerPolynomialMutation(0.1, 2.0), new PolynomialMutation(0.1, 20.0)));

    int populationSize = 100;
    int offspringPopulationSize = 100;

    Termination termination = new TerminationByEvaluations(25000);

    NSGAII<CompositeSolution> algorithm;
    algorithm =
        new NSGAII<>(
            problem, populationSize, offspringPopulationSize, crossover, mutation, termination);

    EvaluationObserver evaluationObserver = new EvaluationObserver(1000);
    RunTimeChartObserver<DoubleSolution> runTimeChartObserver =
        new RunTimeChartObserver<>("NSGA-II", 80, 100, null);

    algorithm.getObservable().register(evaluationObserver);
    algorithm.getObservable().register(runTimeChartObserver);

    algorithm.run();

    new SolutionListOutput(algorithm.getResult())
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.csv", ","))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.csv", ","))
            .print();

    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.csv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.csv");

    System.exit(0);
  }
}
