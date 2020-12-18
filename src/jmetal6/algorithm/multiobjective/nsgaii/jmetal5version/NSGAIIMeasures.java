package jmetal6.algorithm.multiobjective.nsgaii.jmetal5version;

import java.util.Comparator;
import java.util.List;

import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.Problem;
import jmetal6.qualityindicator.impl.hypervolume.PISAHypervolume;
import jmetal6.solution.Solution;
import jmetal6.util.SolutionListUtils;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.front.Front;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.measure.Measurable;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;
import jmetal6.util.measure.impl.DurationMeasure;
import jmetal6.util.measure.impl.SimpleMeasureManager;
import jmetal6.util.solutionattribute.Ranking;
import jmetal6.util.solutionattribute.impl.DominanceRanking;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class NSGAIIMeasures<S extends Solution<?>> extends NSGAII<S> implements Measurable {
  protected CountingMeasure evaluations ;
  protected DurationMeasure durationMeasure ;
  protected SimpleMeasureManager measureManager ;

  protected BasicMeasure<List<S>> solutionListMeasure ;
  protected BasicMeasure<Integer> numberOfNonDominatedSolutionsInPopulation ;
  protected BasicMeasure<Double> hypervolumeValue ;

  protected Front referenceFront ;

  /**
   * Constructor
   */
  public NSGAIIMeasures(Problem<S> problem, int maxIterations, int populationSize,
                        int matingPoolSize, int offspringPopulationSize,
                        CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                        SelectionOperator<List<S>, S> selectionOperator, Comparator<S> dominanceComparator, SolutionListEvaluator<S> evaluator) {
    super(problem, maxIterations, populationSize, matingPoolSize, offspringPopulationSize,
            crossoverOperator, mutationOperator, selectionOperator, dominanceComparator, evaluator) ;

    referenceFront = new ArrayFront() ;

    initMeasures() ;
  }

  @Override protected void initProgress() {
    evaluations.reset(getMaxPopulationSize());
  }

  @Override protected void updateProgress() {
    evaluations.increment(getMaxPopulationSize());

    solutionListMeasure.push(getPopulation());

    if (referenceFront.getNumberOfPoints() > 0) {
      hypervolumeValue.push(
              new PISAHypervolume<S>(referenceFront).evaluate(
                  SolutionListUtils.getNonDominatedSolutions(getPopulation())));
    }
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
    numberOfNonDominatedSolutionsInPopulation = new BasicMeasure<>() ;
    solutionListMeasure = new BasicMeasure<>() ;
    hypervolumeValue = new BasicMeasure<>() ;

    measureManager = new SimpleMeasureManager() ;
    measureManager.setPullMeasure("currentExecutionTime", durationMeasure);
    measureManager.setPullMeasure("currentEvaluation", evaluations);
    measureManager.setPullMeasure("numberOfNonDominatedSolutionsInPopulation",
        numberOfNonDominatedSolutionsInPopulation);

    measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
    measureManager.setPushMeasure("currentEvaluation", evaluations);
    measureManager.setPushMeasure("hypervolume", hypervolumeValue);
  }

  @Override
  public MeasureManager getMeasureManager() {
    return measureManager ;
  }

  @Override protected List<S> replacement(List<S> population,
      List<S> offspringPopulation) {
    List<S> pop = super.replacement(population, offspringPopulation) ;

    Ranking<S> ranking = new DominanceRanking<S>(dominanceComparator);
    ranking.computeRanking(population);

    numberOfNonDominatedSolutionsInPopulation.set(ranking.getSubFront(0).size());

    return pop;
  }

  public CountingMeasure getEvaluations() {
    return evaluations;
  }

  @Override public String getName() {
    return "NSGAIIM" ;
  }

  @Override public String getDescription() {
    return "Nondominated Sorting Genetic Algorithm version II. Version using measures" ;
  }

  public void setReferenceFront(Front referenceFront) {
    this.referenceFront = referenceFront ;
  }
}
