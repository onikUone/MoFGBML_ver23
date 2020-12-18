package jmetal6.example.multiobjective.nsgaii.jmetal5version;

import java.util.List;

import jmetal6.algorithm.DynamicAlgorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.DynamicNSGAII;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.DynamicProblem;
import jmetal6.problem.multiobjective.fda.FDA2;
import jmetal6.qualityindicator.impl.CoverageFront;
import jmetal6.qualityindicator.impl.InvertedGenerationalDistance;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.observable.impl.DefaultObservable;
import jmetal6.util.observer.impl.RunTimeForDynamicProblemsChartObserver;
import jmetal6.util.point.PointSolution;
import jmetal6.util.restartstrategy.impl.CreateNRandomSolutions;
import jmetal6.util.restartstrategy.impl.DefaultRestartStrategy;
import jmetal6.util.restartstrategy.impl.RemoveNRandomSolutions;

public class DynamicNSGAIIRunner {
  /**
   * main() method to run the algorithm as a process
   *
   * @param args
   */
  public static void main(String[] args) {
    DynamicProblem<DoubleSolution, Integer> problem = new FDA2();

    // STEP 2. Create the algorithm
    CrossoverOperator<DoubleSolution> crossover = new SBXCrossover(0.9, 20.0);
    MutationOperator<DoubleSolution> mutation =
        new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0);
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection =
        new BinaryTournamentSelection<>();

    InvertedGenerationalDistance<PointSolution> igd = new InvertedGenerationalDistance<>();
    CoverageFront<PointSolution> coverageFront = new CoverageFront<>(0.055, igd);
    DynamicAlgorithm<List<DoubleSolution>> algorithm =
        new DynamicNSGAII<>(
            problem,
            25000,
            100,
            100,
            100,
            crossover,
            mutation,
            selection,
            new SequentialSolutionListEvaluator<>(),
            new DefaultRestartStrategy<>(
                new RemoveNRandomSolutions<>(10), new CreateNRandomSolutions<>()),
            new DefaultObservable<>("Dynamic NSGA-II"),
            coverageFront);

    // EvaluationObserver evaluationObserver = new EvaluationObserver(1000);
    RunTimeForDynamicProblemsChartObserver<DoubleSolution> runTimeChartObserver =
        new RunTimeForDynamicProblemsChartObserver<>("Dynamic NSGA-II", 80);

    // algorithm.getObservable().register(evaluationObserver);
    algorithm.getObservable().register(runTimeChartObserver);

    algorithm.run();
  }
}
