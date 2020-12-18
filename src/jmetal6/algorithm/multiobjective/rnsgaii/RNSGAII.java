package jmetal6.algorithm.multiobjective.rnsgaii;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.InteractiveAlgorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAII;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.RankingAndPreferenceSelection;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.comparator.DominanceComparator;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.measure.Measurable;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;
import jmetal6.util.measure.impl.DurationMeasure;
import jmetal6.util.measure.impl.SimpleMeasureManager;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class RNSGAII<S extends Solution<?>> extends NSGAII<S> implements
    InteractiveAlgorithm<S,List<S>>, Measurable {

  private List<Double> interestPoint;
  private double epsilon;

  protected SimpleMeasureManager measureManager ;
  protected BasicMeasure<List<S>> solutionListMeasure ;
  protected CountingMeasure evaluations ;
  protected DurationMeasure durationMeasure ;

  /**
   * Constructor
   */
  public RNSGAII(Problem<S> problem, int maxEvaluations, int populationSize,
                 int matingPoolSize, int offspringPopulationSize,
                 CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                 SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator,
                 List<Double> interestPoint, double epsilon) {
    super(problem,maxEvaluations, populationSize,matingPoolSize, offspringPopulationSize, crossoverOperator,
            mutationOperator,selectionOperator, new DominanceComparator<S>(), evaluator);
    this.interestPoint = interestPoint;
    this.epsilon = epsilon;

    measureManager = new SimpleMeasureManager() ;
    measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
    measureManager.setPushMeasure("currentEvaluation", evaluations);

    initMeasures();
  }
  @Override
  public void updatePointOfInterest(List<Double> newReferencePoints){
    this.interestPoint = newReferencePoints;
  }
  @Override protected void initProgress() {
    evaluations.reset(getMaxPopulationSize()) ;
  }

  @Override protected void updateProgress() {
    evaluations.increment(getMaxPopulationSize());
    solutionListMeasure.push(getPopulation());
  }

  @Override protected boolean isStoppingConditionReached() {
    return evaluations.get() >= maxEvaluations;
  }

  @Override
  public void run() {
    durationMeasure.reset();
    durationMeasure.start();
    super.run();
    durationMeasure.stop();
  }

  /* Measures code */
  private void initMeasures() {
    durationMeasure = new DurationMeasure() ;
    evaluations = new CountingMeasure(0) ;
    solutionListMeasure = new BasicMeasure<>() ;

    measureManager = new SimpleMeasureManager() ;
    measureManager.setPullMeasure("currentExecutionTime", durationMeasure);
    measureManager.setPullMeasure("currentEvaluation", evaluations);

    measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
    measureManager.setPushMeasure("currentEvaluation", evaluations);
  }

  @Override
  public MeasureManager getMeasureManager() {
    return measureManager ;
  }

  @Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(population);
    jointPopulation.addAll(offspringPopulation);

    RankingAndPreferenceSelection<S> rankingAndCrowdingSelection ;
    rankingAndCrowdingSelection = new RankingAndPreferenceSelection<S>(getMaxPopulationSize(), interestPoint, epsilon) ;

    return rankingAndCrowdingSelection.execute(jointPopulation) ;
  }

  @Override public String getName() {
    return "RNSGAII" ;
  }

  @Override public String getDescription() {
    return "Reference Point Based Nondominated Sorting Genetic Algorithm version II" ;
  }
}
