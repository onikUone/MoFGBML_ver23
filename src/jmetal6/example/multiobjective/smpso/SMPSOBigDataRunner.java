package jmetal6.example.multiobjective.smpso;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.smpso.SMPSO;
import jmetal6.algorithm.multiobjective.smpso.SMPSOBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.cec2015OptBigDataCompetition.BigOpt2015;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.archive.BoundedArchive;
import jmetal6.util.archive.impl.CrowdingDistanceArchive;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * Class for configuring and running the SMPSO algorithm to solve a problem of the CEC2015
 * Big Optimization competition
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class SMPSOBigDataRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws jmetal6.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.smpso.SMPSOBigDataRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws Exception {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    MutationOperator<DoubleSolution> mutation;

    String instanceName ;

    if (args.length == 1) {
      instanceName = args[0] ;
    } else {
      instanceName = "D12" ;
    }

    problem = new BigOpt2015(instanceName) ;

    BoundedArchive<DoubleSolution> archive = new CrowdingDistanceArchive<DoubleSolution>(20) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    algorithm = new SMPSOBuilder(problem, archive)
            .setMutation(mutation)
            .setMaxIterations(250)
            .setSwarmSize(20)
     //       .setRandomGenerator(new MersenneTwisterGenerator())
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<DoubleSolution> population = ((SMPSO)algorithm).getResult();
    long computingTime = algorithmRunner.getComputingTime();

    new SolutionListOutput(population)
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed()) ;
  }
}
