package jmetal6.example.multiobjective.spea2;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.spea2.SPEA2Builder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SinglePointCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.ProblemUtils;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;

/**
 * Class for configuring and running the SPEA2 algorithm (binary encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class SPEA2BinaryRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.spea2.SPEA2BinaryRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    BinaryProblem problem;
    Algorithm<List<BinarySolution>> algorithm;
    CrossoverOperator<BinarySolution> crossover;
    MutationOperator<BinarySolution> mutation;
    SelectionOperator<List<BinarySolution>, BinarySolution> selection;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.OneZeroMax";
      referenceParetoFront = "" ;
    }

    problem = (BinaryProblem)ProblemUtils.<BinarySolution> loadProblem(problemName);

    double crossoverProbability = 0.9 ;
    crossover = new SinglePointCrossover(crossoverProbability) ;

    double mutationProbability = 1.0 / problem.getTotalNumberOfBits() ;
    mutation = new BitFlipMutation(mutationProbability) ;

    selection = new BinaryTournamentSelection<BinarySolution>(new RankingAndCrowdingDistanceComparator<BinarySolution>());

    algorithm = new SPEA2Builder<>(problem, crossover, mutation)
        .setSelectionOperator(selection)
        .setMaxIterations(250)
        .setPopulationSize(100)
        .build() ;

    new AlgorithmRunner.Executor(algorithm).execute() ;

    List<BinarySolution> population = algorithm.getResult();

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
}
