package jmetal6.algorithm.multiobjective.nsgaii;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal6.algorithm.impl.AbstractEvolutionaryAlgorithm;
import jmetal6.component.densityestimator.DensityEstimator;
import jmetal6.component.densityestimator.impl.CrowdingDistanceDensityEstimator;
import jmetal6.component.evaluation.Evaluation;
import jmetal6.component.evaluation.impl.SequentialEvaluation;
import jmetal6.component.initialsolutioncreation.InitialSolutionsCreation;
import jmetal6.component.initialsolutioncreation.impl.RandomSolutionsCreation;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.FastNonDominatedSortRanking;
import jmetal6.component.replacement.Replacement;
import jmetal6.component.replacement.impl.RankingAndDensityEstimatorReplacement;
import jmetal6.component.selection.MatingPoolSelection;
import jmetal6.component.selection.impl.NaryTournamentMatingPoolSelection;
import jmetal6.component.termination.Termination;
import jmetal6.component.variation.Variation;
import jmetal6.component.variation.impl.CrossoverAndMutationVariation;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.SolutionListUtils;
import jmetal6.util.comparator.MultiComparator;
import jmetal6.util.observable.Observable;
import jmetal6.util.observable.ObservableEntity;
import jmetal6.util.observable.impl.DefaultObservable;

/** @author Antonio J. Nebro <antonio@lcc.uma.es> */
public class NSGAII<S extends Solution<?>> extends AbstractEvolutionaryAlgorithm<S, List<S>>
    implements ObservableEntity {
  private int evaluations;
  private int populationSize;
  private int offspringPopulationSize;

  protected SelectionOperator<List<S>, S> selectionOperator;
  protected CrossoverOperator<S> crossoverOperator;
  protected MutationOperator<S> mutationOperator;

  private Map<String, Object> algorithmStatusData;

  private InitialSolutionsCreation<S> initialSolutionsCreation;
  private Termination termination;
  private Evaluation<S> evaluation ;
  private Replacement<S> replacement;
  private Variation<S> variation;
  private MatingPoolSelection<S> selection;

  private long startTime;
  private long totalComputingTime;

  private Observable<Map<String, Object>> observable;

  /** Constructor */
  public NSGAII(
      Problem<S> problem,
      int populationSize,
      int offspringPopulationSize,
      CrossoverOperator<S> crossoverOperator,
      MutationOperator<S> mutationOperator,
      Termination termination,
      Ranking<S> ranking) {

    this.populationSize = populationSize;
    this.problem = problem;

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;

    this.initialSolutionsCreation = new RandomSolutionsCreation<>(problem, populationSize);

    DensityEstimator<S> densityEstimator = new CrowdingDistanceDensityEstimator<>();

    this.replacement =
        new RankingAndDensityEstimatorReplacement<>(
            ranking, densityEstimator, Replacement.RemovalPolicy.oneShot);

    this.variation =
        new CrossoverAndMutationVariation<>(
            offspringPopulationSize, crossoverOperator, mutationOperator);

    this.selection =
        new NaryTournamentMatingPoolSelection<>(
            2,
            variation.getMatingPoolSize(),
            new MultiComparator<>(
                Arrays.asList(
                    ranking.getSolutionComparator(), densityEstimator.getSolutionComparator())));

    this.termination = termination;

    this.evaluation = new SequentialEvaluation<>();
    this.offspringPopulationSize = offspringPopulationSize;

    this.algorithmStatusData = new HashMap<>();

    this.observable = new DefaultObservable<>("NSGAII algorithm");
  }

  /** Constructor */
  public NSGAII(
      Problem<S> problem,
      int populationSize,
      int offspringPopulationSize,
      CrossoverOperator<S> crossoverOperator,
      MutationOperator<S> mutationOperator,
      Termination termination) {
    this(
        problem,
        populationSize,
        offspringPopulationSize,
        crossoverOperator,
        mutationOperator,
        termination,
        new FastNonDominatedSortRanking<>());
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
    return  evaluation.evaluate(population, getProblem());
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
    return this.selection.select(population);
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
  protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    List<S> newPopulation = replacement.replace(population, offspringPopulation);

    return newPopulation;
  }

  @Override
  public List<S> getResult() {
    return SolutionListUtils.getNonDominatedSolutions(getPopulation());
  }

  @Override
  public String getName() {
    return "NSGAII";
  }

  @Override
  public String getDescription() {
    return "Nondominated Sorting Genetic Algorithm version II";
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

  public NSGAII<S> setEvaluation(Evaluation<S> evaluation) {
    this.evaluation = evaluation ;

    return this;
  }

  public NSGAII<S> setInitialSolutionsCreation(
      InitialSolutionsCreation<S> initialSolutionsCreation) {
    this.initialSolutionsCreation = initialSolutionsCreation;

    return this;
  }
}
