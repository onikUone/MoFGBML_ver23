package jmetal6.algorithm.multiobjective.moead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal6.algorithm.impl.AbstractEvolutionaryAlgorithm;
import jmetal6.component.evaluation.Evaluation;
import jmetal6.component.evaluation.impl.SequentialEvaluation;
import jmetal6.component.initialsolutioncreation.InitialSolutionsCreation;
import jmetal6.component.replacement.Replacement;
import jmetal6.component.selection.MatingPoolSelection;
import jmetal6.component.termination.Termination;
import jmetal6.component.variation.Variation;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.checking.Check;
import jmetal6.util.observable.Observable;
import jmetal6.util.observable.ObservableEntity;
import jmetal6.util.observable.impl.DefaultObservable;

/**
 * Class implementing a generic MOEA/D algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es> */
public class MOEAD<S extends Solution<?>> extends AbstractEvolutionaryAlgorithm<S, List<S>>
    implements ObservableEntity {

  protected int evaluations;
  protected int populationSize;
  protected int offspringPopulationSize;

  protected InitialSolutionsCreation<S> initialSolutionsCreation;
  protected Termination termination;
  protected Evaluation<S> evaluation;
  protected Replacement<S> replacement;
  protected Variation<S> variation;
  protected MatingPoolSelection<S> selection;

  protected long startTime;
  protected long totalComputingTime;

  protected Map<String, Object> algorithmStatusData;
  protected Observable<Map<String, Object>> observable;

  /** Constructor */
  public MOEAD(
      Problem<S> problem,
      int populationSize,
      InitialSolutionsCreation<S> initialSolutionsCreation,
      Variation<S> variation,
      MatingPoolSelection<S> selection,
      Replacement<S> replacement,
      Termination termination) {

    this.populationSize = populationSize;
    this.problem = problem;

    this.offspringPopulationSize = 1;

    this.variation = variation;
    this.selection = selection;
    this.initialSolutionsCreation = initialSolutionsCreation ;
    this.replacement = replacement ;
    this.termination = termination ;
    this.evaluation = new SequentialEvaluation<>();

    this.algorithmStatusData = new HashMap<>();

    this.observable = new DefaultObservable<>("MOEA/D algorithm");
  }

  /**
   * Empty constructor that creates an empty instance. It is intended to allow the definition of different subclass
   * constructors. It is up to the developer the correct creation of the algorithm components.
   */
  public MOEAD() {
  }

  @Override
  public void run() {
    startTime = System.currentTimeMillis();
    super.run();
    totalComputingTime = System.currentTimeMillis() - startTime;
  }

  @Override
  protected void initProgress() {
    evaluations = populationSize;

    algorithmStatusData.put("EVALUATIONS", evaluations);
    algorithmStatusData.put("POPULATION", population);
    algorithmStatusData.put("COMPUTING_TIME", System.currentTimeMillis() - startTime);

    observable.setChanged();
    observable.notifyObservers(algorithmStatusData);
  }

  @Override
  protected void updateProgress() {
    evaluations += offspringPopulationSize;
    algorithmStatusData.put("EVALUATIONS", evaluations);
    algorithmStatusData.put("POPULATION", population);
    algorithmStatusData.put("COMPUTING_TIME", System.currentTimeMillis() - startTime);

    observable.setChanged();
    observable.notifyObservers(algorithmStatusData);
  }

  @Override
  protected boolean isStoppingConditionReached() {
    return termination.isMet(algorithmStatusData);
  }

  @Override
  protected List<S> createInitialPopulation() {
    return initialSolutionsCreation.create();
  }

  @Override
  protected List<S> evaluatePopulation(List<S> population) {
    return evaluation.evaluate(population, getProblem());
  }

  /**
   * This method iteratively applies a {@link SelectionOperator} to the population to fill the
   * mating pool population.
   *
   * @param population
   * @return The mating pool population
   */
  @Override
  protected List<S> selection(List<S> population) {
    List<S> matingPool = selection.select(population);

    Check.that(
        matingPool.size() == variation.getMatingPoolSize(),
        "The mating pool size is "
            + matingPool.size()
            + " instead of "
            + variation.getMatingPoolSize());

    return matingPool;
  }

  /**
   * This methods iteratively applies a {@link CrossoverOperator} a {@link MutationOperator} to the
   * population to create the offspring population. The population size must be divisible by the
   * number of parents required by the {@link CrossoverOperator}; this way, the needed parents are
   * taken sequentially from the population.
   *
   * <p>The number of solutions returned by the {@link CrossoverOperator} must be equal to the
   * offspringPopulationSize state variable
   *
   * @param matingPool
   * @return The new created offspring population
   */
  @Override
  protected List<S> reproduction(List<S> matingPool) {
    return variation.variate(population, matingPool);
  }

  @Override
  protected List<S> replacement(
      List<S> population, List<S> offspringPopulation) {
    return replacement.replace(population, offspringPopulation) ;
  }

  @Override
  public List<S> getResult() {
    return population;
  }

  @Override
  public String getName() {
    return "MOEA/D";
  }

  @Override
  public String getDescription() {
    return "MOEA/D";
  }

  public Map<String, Object> getAlgorithmStatusData() {
    return algorithmStatusData;
  }

  @Override
  public Observable<Map<String, Object>> getObservable() {
    return observable;
  }

  public long getTotalComputingTime() {
    return totalComputingTime;
  }

  public long getEvaluations() {
    return evaluations;
  }

  public MOEAD setEvaluation(Evaluation<S> evaluation) {
    this.evaluation = evaluation;

    return this;
  }

  public MOEAD setInitialSolutionsCreation(
      InitialSolutionsCreation<S> initialSolutionsCreation) {
    this.initialSolutionsCreation = initialSolutionsCreation;

    return this;
  }
}
