package jmetal6.example.multiobjective;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.mochc.MOCHC45;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.HUXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.RandomSelection;
import jmetal6.operator.selection.impl.RankingAndCrowdingSelection;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * This class executes the algorithm described in:
 * A.J. Nebro, E. Alba, G. Molina, F. Chicano, F. Luna, J.J. Durillo
 * "Optimal antenna placement using a new multi-objective chc algorithm".
 * GECCO '07: Proceedings of the 9th annual conference on Genetic and
 * evolutionary computation. London, England. July 2007.
 */
public class MOCHC45Runner extends AbstractAlgorithmRunner {
  public static void main(String[] args) throws Exception {
    CrossoverOperator<BinarySolution> crossoverOperator;
    MutationOperator<BinarySolution> mutationOperator;
    SelectionOperator<List<BinarySolution>, BinarySolution> parentsSelection;
    SelectionOperator<List<BinarySolution>, List<BinarySolution>> newGenerationSelection;
    Algorithm<List<BinarySolution>> algorithm ;

    BinaryProblem problem ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT5";
    }

    problem = (BinaryProblem) ProblemUtils.<BinarySolution> loadProblem(problemName);

    crossoverOperator = new HUXCrossover(1.0) ;
    parentsSelection = new RandomSelection<BinarySolution>() ;
    newGenerationSelection = new RankingAndCrowdingSelection<BinarySolution>(100) ;
    mutationOperator = new BitFlipMutation(0.35) ;

    algorithm = new MOCHC45(problem, 100, 25000, 3, 0.05, 0.25, crossoverOperator, mutationOperator,
        newGenerationSelection, parentsSelection, new SequentialSolutionListEvaluator<BinarySolution>()) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute() ;

    List<BinarySolution> population = (algorithm).getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    //if (!referenceParetoFront.equals("")) {
    //  printQualityIndicators(population, referenceParetoFront) ;
    //}
  }
}
