package jmetal6.example.multiobjective.smpso;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.smpso.SMPSO;
import jmetal6.algorithm.multiobjective.smpso.SMPSOBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.lz09.LZ09F2;
import jmetal6.qualityindicator.impl.hypervolume.PISAHypervolume;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.archive.BoundedArchive;
import jmetal6.util.archive.impl.HypervolumeArchive;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.pseudorandom.impl.MersenneTwisterGenerator;

/**
 * Class for configuring and running the SMPSO algorithm using an HypervolumeArchive, i.e, the
 * SMPSOhv algorithm described in:
 * A.J Nebro, J.J. Durillo, C.A. Coello Coello. Analysis of Leader Selection Strategies in a
 * Multi-Objective Particle Swarm Optimizer. 2013 IEEE Congress on Evolutionary Computation. June 2013
 * DOI: 10.1109/CEC.2013.6557955
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SMPSOHvRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws jmetal6.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.smpso.SMPSOHvRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws Exception {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    MutationOperator<DoubleSolution> mutation;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";
      referenceParetoFront = "" ;
    }
    System.out.println("Warning: the problem name is not used anymore and may be removed later.") ;
    System.out.println("Warning: current problem name: " + problemName) ;

    problem = new LZ09F2() ;

    BoundedArchive<DoubleSolution> archive =
        new HypervolumeArchive<DoubleSolution>(100, new PISAHypervolume<DoubleSolution>()) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    algorithm = new SMPSOBuilder(problem, archive)
        .setMutation(mutation)
        .setMaxIterations(1750)
        .setSwarmSize(100)
        .setRandomGenerator(new MersenneTwisterGenerator())
        .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
        .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute();

    List<DoubleSolution> population = ((SMPSO)algorithm).getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
}
