package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.evolutionstrategy.EvolutionStrategyBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.problem.singleobjective.OneMax;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a non elitist (mu,lamba) evolution strategy. The target problem is
 * OneMax.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NonElitistEvolutionStrategyRunner {
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.NonElitistEvolutionStrategyRunner
   */
  public static void main(String[] args) throws Exception {

    Algorithm<BinarySolution> algorithm;
    BinaryProblem problem = new OneMax(512) ;

    MutationOperator<BinarySolution> mutationOperator = new BitFlipMutation(1.0 / problem.getBitsFromVariable(0)) ;

    algorithm = new EvolutionStrategyBuilder<BinarySolution>(problem, mutationOperator,
        EvolutionStrategyBuilder.EvolutionStrategyVariant.NON_ELITIST)
        .setMaxEvaluations(25000)
        .setMu(1)
        .setLambda(10)
        .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    BinarySolution solution = algorithm.getResult() ;
    List<BinarySolution> population = new ArrayList<>(1) ;
    population.add(solution) ;

    long computingTime = algorithmRunner.getComputingTime() ;

    new SolutionListOutput(population)
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

  }
}
