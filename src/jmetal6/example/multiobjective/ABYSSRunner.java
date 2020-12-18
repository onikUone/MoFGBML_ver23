package jmetal6.example.multiobjective;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.abyss.ABYSSBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.archive.Archive;
import jmetal6.util.archive.impl.CrowdingDistanceArchive;

/**
 * This class is the main program used to configure and run AbYSS, a
 * multiobjective scatter search metaheuristics, which is described in:
 *   A.J. Nebro, F. Luna, E. Alba, B. Dorronsoro, J.J. Durillo, A. Beham
 *   "AbYSS: Adapting Scatter Search to Multiobjective Optimization."
 *   IEEE Transactions on Evolutionary Computation. Vol. 12,
 *   No. 4 (August 2008), pp. 439-457
 *
 *   @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ABYSSRunner extends AbstractAlgorithmRunner {

  /**
   * @param args Command line arguments.
   * @throws FileNotFoundException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.AbYSSRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws Exception {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    String problemName ;

    String referenceParetoFront = "" ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "referenceFronts/ZDT1.pf" ;
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

    Archive<DoubleSolution> archive = new CrowdingDistanceArchive<DoubleSolution>(100) ;

    algorithm = new ABYSSBuilder(problem, archive)
        .setMaxEvaluations(25000)
        .build();

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
