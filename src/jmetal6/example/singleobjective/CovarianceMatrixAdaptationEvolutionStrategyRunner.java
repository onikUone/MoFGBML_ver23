package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.evolutionstrategy.CovarianceMatrixAdaptationEvolutionStrategy;
import jmetal6.example.AlgorithmRunner;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.singleobjective.Sphere;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 */
public class CovarianceMatrixAdaptationEvolutionStrategyRunner {
  /**
   */
  public static void main(String[] args) throws Exception {

    Algorithm<DoubleSolution> algorithm;
    DoubleProblem problem = new Sphere() ;

    algorithm = new CovarianceMatrixAdaptationEvolutionStrategy.Builder(problem)
            .build() ;


    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    DoubleSolution solution = algorithm.getResult() ;
    List<DoubleSolution> population = new ArrayList<>(1) ;
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
