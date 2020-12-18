package jmetal6.example.multiobjective;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.dmopso.DMOPSO;
import jmetal6.algorithm.multiobjective.dmopso.DMOPSO.FunctionType;
import jmetal6.example.AlgorithmRunner;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;

/**
 * Class for configuring and running the DMOPSO algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class DMOPSORunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws jmetal6.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.DMOPSORunner problemName [referenceFront]
   */
  public static void main(String[] args) throws Exception {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "../jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf" ;
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

    algorithm = new DMOPSO(problem, 100, 250, 0.0, 0.1, 0.0, 1.0, 1.5, 2.5, 1.5, 2.5, 0.1, 0.4, -1.0, -1.0,
            FunctionType.TCHE, "MOEAD_Weights", 2) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
}
