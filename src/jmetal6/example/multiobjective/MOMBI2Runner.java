package jmetal6.example.multiobjective;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.mombi.MOMBI2;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Class to configure and run the MOMBI2 algorithm
 *
 * @author Juan J. Durillo <juan@dps.uibk.ac.at>
 *     <p>Reference: Improved Metaheuristic Based on the R2 Indicator for Many-Objective
 *     Optimization. R. Hernández Gómez, C.A. Coello Coello. Proceeding GECCO '15 Proceedings of the
 *     2015 on Genetic and Evolutionary Computation Conference. Pages 679-686 DOI:
 *     10.1145/2739480.2754776
 */
public class MOMBI2Runner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.MOMBIRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
    String referenceParetoFront = "";

    String problemName;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";
      referenceParetoFront = "referenceFronts/DTLZ1.3D.pf";
    }

    problem = ProblemUtils.<DoubleSolution>loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    selection =
        new BinaryTournamentSelection<DoubleSolution>(
            new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    algorithm =
        new MOMBI2<>(
            problem,
            750,
            crossover,
            mutation,
            selection,
            new SequentialSolutionListEvaluator<DoubleSolution>(),
            "mombi2-weights/weight/weight_03D_12.sld");
    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }
}
