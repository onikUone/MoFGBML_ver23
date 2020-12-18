package jmetal6.example.multiobjective.nsgaii;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import jmetal6.algorithm.multiobjective.nsgaii.NSGAII;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.FastNonDominatedSortRanking;
import jmetal6.component.termination.Termination;
import jmetal6.component.termination.impl.TerminationByEvaluations;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.ProblemUtils;
import jmetal6.util.comparator.GDominanceComparator;
import jmetal6.util.observer.impl.RunTimeChartObserver;

/**
 * Class to configure and run the NSGA-II algorithm using a {@link GDominanceComparator}, which
 * allows empower NSGA-II with a preference articulation mechanism based on reference point.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class GNSGAIIExample extends AbstractAlgorithmRunner {

  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    NSGAII<DoubleSolution> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;

    String problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
    String referenceParetoFront = "referenceFronts/ZDT1.pf";

    problem = ProblemUtils.<DoubleSolution>loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    int populationSize = 100;
    int offspringPopulationSize = 100;

    Termination termination = new TerminationByEvaluations(25000);

    List<Double> referencePoint = Arrays.asList(0.1, 0.5);
    Comparator<DoubleSolution> dominanceComparator = new GDominanceComparator<>(referencePoint);
    Ranking<DoubleSolution> ranking = new FastNonDominatedSortRanking<>(dominanceComparator);

    algorithm =
        new NSGAII<>(
            problem,
            populationSize,
            offspringPopulationSize,
            crossover,
            mutation,
            termination,
            ranking);

    RunTimeChartObserver<DoubleSolution> runTimeChartObserver =
        new RunTimeChartObserver<>("NSGA-II", 80, referenceParetoFront);
    algorithm.getObservable().register(runTimeChartObserver);

    runTimeChartObserver.setReferencePointList(Arrays.asList(referencePoint));

    algorithm.run();

    System.exit(0);
  }
}
