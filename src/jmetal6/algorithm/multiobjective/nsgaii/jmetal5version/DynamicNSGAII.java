package jmetal6.algorithm.multiobjective.nsgaii.jmetal5version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal6.algorithm.DynamicAlgorithm;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.DynamicProblem;
import jmetal6.qualityindicator.impl.CoverageFront;
import jmetal6.solution.Solution;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.front.Front;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.observable.Observable;
import jmetal6.util.point.PointSolution;
import jmetal6.util.restartstrategy.RestartStrategy;

public class DynamicNSGAII<S extends Solution<?>> extends NSGAII<S>
    implements DynamicAlgorithm<List<S>> {

  private RestartStrategy<S> restartStrategy;
  private DynamicProblem<S, Integer> problem;
  private Observable<Map<String, Object>> observable;
  private int completedIterations;
  private CoverageFront<PointSolution> coverageFront;
  private List<S> lastReceivedFront;
  /**
   * Constructor
   *
   * @param problem
   * @param maxEvaluations
   * @param populationSize
   * @param matingPoolSize
   * @param offspringPopulationSize
   * @param crossoverOperator
   * @param mutationOperator
   * @param selectionOperator
   * @param evaluator
   * @param restartStrategy
   * @param observable
   */
  public DynamicNSGAII(
      DynamicProblem<S, Integer> problem,
      int maxEvaluations,
      int populationSize,
      int matingPoolSize,
      int offspringPopulationSize,
      CrossoverOperator<S> crossoverOperator,
      MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator,
      SolutionListEvaluator<S> evaluator,
      RestartStrategy<S> restartStrategy,
      Observable<Map<String, Object>> observable,
      CoverageFront<PointSolution> coverageFront) {
    super(
        problem,
        maxEvaluations,
        populationSize,
        matingPoolSize,
        offspringPopulationSize,
        crossoverOperator,
        mutationOperator,
        selectionOperator,
        evaluator);
    this.restartStrategy = restartStrategy;
    this.problem = problem;
    this.observable = observable;
    this.completedIterations = 0;
    this.coverageFront = coverageFront;
    this.lastReceivedFront = null;
  }

  @Override
  protected boolean isStoppingConditionReached() {
    if (evaluations >= maxEvaluations) {

      boolean coverage = false;
      if (lastReceivedFront != null) {
        Front referenceFront = new ArrayFront(lastReceivedFront);
        coverageFront.updateFront(referenceFront);
        List<PointSolution> pointSolutionList = new ArrayList<>();
        List<S> list = getPopulation();
        for (S s : list) {
          PointSolution pointSolution = new PointSolution(s);
          pointSolutionList.add(pointSolution);
        }
        coverage = coverageFront.isCoverageWithLast(pointSolutionList);

      }

      if (coverage) {
        observable.setChanged();

        Map<String, Object> algorithmData = new HashMap<>();

        algorithmData.put("EVALUATIONS", completedIterations);
        algorithmData.put("POPULATION", getPopulation());

        observable.notifyObservers(algorithmData);
        observable.clearChanged();
      }
      lastReceivedFront = getPopulation();
      completedIterations++;
      problem.update(completedIterations);

      restart();
      evaluator.evaluate(getPopulation(), getDynamicProblem());

      initProgress();
    }
    return false;
  }

  @Override
  protected void updateProgress() {
    super.updateProgress();
  }

  @Override
  public DynamicProblem<S, ?> getDynamicProblem() {
    return problem;
  }

  @Override
  public void restart() {
    this.restartStrategy.restart(getPopulation(), (DynamicProblem<S, ?>) getProblem());
  }

  @Override
  public RestartStrategy<?> getRestartStrategy() {
    return restartStrategy;
  }

  @Override
  public Observable<Map<String, Object>> getObservable() {
    return observable;
  }
}
