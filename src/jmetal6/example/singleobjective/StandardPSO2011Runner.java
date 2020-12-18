package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.particleswarmoptimization.StandardPSO2011;
import jmetal6.example.AlgorithmRunner;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.evaluator.impl.MultithreadedSolutionListEvaluator;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a StandardPSO2007. The algorithm can be configured to use threads. The
 * number of cores is specified as an optional parameter. The target problem is Sphere.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class StandardPSO2011Runner {
  private static final int DEFAULT_NUMBER_OF_CORES = 1;

  /** Usage: java org.uma.jmetal.runner.singleobjective.StandardPSO2007Runner [cores] */
  public static void main(String[] args) throws Exception {

    DoubleProblem problem;
    Algorithm<DoubleSolution> algorithm;
    SolutionListEvaluator<DoubleSolution> evaluator;

    String problemName = "org.uma.jmetal.problem.singleobjective.Sphere";

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

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

    algorithm =
        new StandardPSO2011(
            problem,
            10 + (int) (2 * Math.sqrt(problem.getNumberOfVariables())),
            25000,
            3,
            evaluator);

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
    JMetalLogger.logger.info("Solution: " + solution.getVariable(0));
    evaluator.shutdown();
  }
}
