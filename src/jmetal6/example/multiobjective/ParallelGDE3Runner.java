package jmetal6.example.multiobjective;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.gde3.GDE3;
import jmetal6.algorithm.multiobjective.gde3.GDE3Builder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.selection.impl.DifferentialEvolutionSelection;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.evaluator.impl.MultithreadedSolutionListEvaluator;

/**
 * Class for configuring and running the GDE3 algorithm (parallel version)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ParallelGDE3Runner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.ParallelGDE3Runner problemName [referenceFront]
   */
  public static void main(String[] args) throws FileNotFoundException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    DifferentialEvolutionSelection selection;
    DifferentialEvolutionCrossover crossover;

    String referenceParetoFront = "";

    String problemName;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "referenceFronts/ZDT1.pf";
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    double cr = 0.5;
    double f = 0.5;
    crossover =
        new DifferentialEvolutionCrossover(
            cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);
    selection = new DifferentialEvolutionSelection();

    SolutionListEvaluator<DoubleSolution> evaluator =
        new MultithreadedSolutionListEvaluator<DoubleSolution>(0);

    algorithm =
        new GDE3Builder(problem)
            .setCrossover(crossover)
            .setSelection(selection)
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .setSolutionSetEvaluator(evaluator)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<DoubleSolution> population = ((GDE3) algorithm).getResult();
    long computingTime = algorithmRunner.getComputingTime();

    evaluator.shutdown();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }
}
