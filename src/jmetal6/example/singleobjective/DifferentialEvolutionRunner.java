package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.differentialevolution.DifferentialEvolutionBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.selection.impl.DifferentialEvolutionSelection;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.singleobjective.Sphere;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.evaluator.impl.MultithreadedSolutionListEvaluator;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a differential evolution algorithm. The algorithm can be configured to
 * use threads. The number of cores is specified as an optional parameter. The target problem is
 * Sphere.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class DifferentialEvolutionRunner {
  private static final int DEFAULT_NUMBER_OF_CORES = 1;

  /** Usage: java org.uma.jmetal.runner.singleobjective.DifferentialEvolutionRunner [cores] */
  public static void main(String[] args) throws Exception {

    DoubleProblem problem;
    Algorithm<DoubleSolution> algorithm;
    DifferentialEvolutionSelection selection;
    DifferentialEvolutionCrossover crossover;
    SolutionListEvaluator<DoubleSolution> evaluator;

    problem = new Sphere(20);

    int numberOfCores;
    if (args.length == 1) {
      numberOfCores = Integer.valueOf(args[0]);
    } else {
      numberOfCores = DEFAULT_NUMBER_OF_CORES;
    }

    if (numberOfCores == 1) {
      evaluator = new SequentialSolutionListEvaluator<DoubleSolution>();
    } else {
      evaluator = new MultithreadedSolutionListEvaluator<DoubleSolution>(numberOfCores);
    }

    crossover =
        new DifferentialEvolutionCrossover(
            0.5, 0.5, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);
    selection = new DifferentialEvolutionSelection();

    algorithm =
        new DifferentialEvolutionBuilder(problem)
            .setCrossover(crossover)
            .setSelection(selection)
            .setSolutionListEvaluator(evaluator)
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    DoubleSolution solution = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    List<DoubleSolution> population = new ArrayList<>(1);
    population.add(solution);
    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

    JMetalLogger.logger.info("Fitness: " + solution.getObjective(0));

    evaluator.shutdown();
  }
}
