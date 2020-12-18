package jmetal6.example.singleobjective;

import java.util.Comparator;

import jmetal6.algorithm.impl.DefaultLocalSearch;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.problem.singleobjective.OneMax;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.comparator.DominanceComparator;

/**
 * Class to configure and run a single objective local search. The target problem is OneMax.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class LocalSearchRunner {
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.LocalSearchRunner
   */
  public static void main(String[] args) throws Exception {
    BinaryProblem problem = new OneMax(1024) ;

    MutationOperator<BinarySolution> mutationOperator =
        new BitFlipMutation(1.0 / problem.getBitsFromVariable(0)) ;

    int improvementRounds = 10000 ;

    Comparator<BinarySolution> comparator = new DominanceComparator<>() ;

    DefaultLocalSearch<BinarySolution> localSearch = new DefaultLocalSearch<BinarySolution>(
            improvementRounds,
            problem,
            mutationOperator,
            comparator) ;

    localSearch.run();

    BinarySolution newSolution = localSearch.getResult() ;

    JMetalLogger.logger.info("Fitness: " + newSolution.getObjective(0)) ;
    JMetalLogger.logger.info("Solution: " + newSolution.getVariable(0)) ;
  }
}
