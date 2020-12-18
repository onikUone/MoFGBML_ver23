package jmetal6.auto.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jmetal6.algorithm.Algorithm;
import jmetal6.auto.component.evaluation.Evaluation;
import jmetal6.auto.component.initialsolutionscreation.InitialSolutionsCreation;
import jmetal6.auto.component.replacement.Replacement;
import jmetal6.auto.component.selection.MatingPoolSelection;
import jmetal6.auto.component.termination.Termination;
import jmetal6.auto.component.variation.Variation;
import jmetal6.solution.Solution;
import jmetal6.util.archive.Archive;
import jmetal6.util.observable.Observable;
import jmetal6.util.observable.ObservableEntity;
import jmetal6.util.observable.impl.DefaultObservable;

public class EvolutionaryAlgorithm<S extends Solution<?>>
    implements Algorithm<List<S>>, ObservableEntity {
  private List<S> population;
  private Archive<S> externalArchive;

  private Evaluation<S> evaluation;
  private InitialSolutionsCreation<S> createInitialPopulation;
  private Termination termination;
  private MatingPoolSelection<S> selection;
  private Variation<S> variation;
  private Replacement<S> replacement;

  private Map<String, Object> attributes;

  private long initTime;
  private long totalComputingTime;
  private int evaluations;
  private Observable<Map<String, Object>> observable;

  private final String name;

  /**
   * Constructor
   *
   * @param name Algorithm name
   * @param evaluation
   * @param initialPopulationCreation
   * @param termination
   * @param selection
   * @param variation
   * @param replacement
   * @param externalArchive
   */
  public EvolutionaryAlgorithm(
      String name,
      Evaluation<S> evaluation,
      InitialSolutionsCreation<S> initialPopulationCreation,
      Termination termination,
      MatingPoolSelection<S> selection,
      Variation<S> variation,
      Replacement<S> replacement,
      Archive<S> externalArchive) {
    this.name = name;
    this.evaluation = evaluation;
    this.createInitialPopulation = initialPopulationCreation;
    this.termination = termination;
    this.selection = selection;
    this.variation = variation;
    this.replacement = replacement;
    this.externalArchive = externalArchive;

    this.observable = new DefaultObservable<>("Evolutionary Algorithm");
    this.attributes = new HashMap<>();
  }

  /**
   * Constructor
   *
   * @param name Algorithm name
   * @param evaluation
   * @param initialPopulationCreation
   * @param termination
   * @param selection
   * @param variation
   * @param replacement
   */
  public EvolutionaryAlgorithm(
      String name,
      Evaluation<S> evaluation,
      InitialSolutionsCreation<S> initialPopulationCreation,
      Termination termination,
      MatingPoolSelection<S> selection,
      Variation<S> variation,
      Replacement<S> replacement) {
    this(
        name,
        evaluation,
        initialPopulationCreation,
        termination,
        selection,
        variation,
        replacement,
        null);
  }

  public void run() {
    initializeAttributes() ;

    population = createInitialPopulation.create();
    population = evaluation.evaluate(population);
    initProgress();
    while (!termination.isMet(attributes)) {
      List<S> matingPopulation = selection.select(population);
      List<S> offspringPopulation = variation.variate(population, matingPopulation);
      offspringPopulation = evaluation.evaluate(offspringPopulation);
      updateArchive(offspringPopulation);

      population = replacement.replace(population, offspringPopulation);
      updateProgress();
    }
  }

  private void initializeAttributes() {
    initTime = System.currentTimeMillis() ;
  }

  private void updateArchive(List<S> population) {
    if (externalArchive != null) {
      for (S solution : population) {
        externalArchive.add(solution);
      }
    }
  }

  protected void initProgress() {
    evaluations = population.size();

    updateArchive(population);

    attributes.put("EVALUATIONS", evaluations);
    attributes.put("POPULATION", population);
    attributes.put("COMPUTING_TIME", getCurrentComputingTime());
  }

  protected void updateProgress() {
    evaluations += variation.getOffspringPopulationSize();

    attributes.put("EVALUATIONS", evaluations);
    attributes.put("POPULATION", population);
    attributes.put("COMPUTING_TIME", getCurrentComputingTime());

    observable.setChanged();
    observable.notifyObservers(attributes);

    totalComputingTime = getCurrentComputingTime() ;
  }

  public long getCurrentComputingTime() {
    return System.currentTimeMillis() - initTime;
  }

  public int getNumberOfEvaluations() {
    return evaluations;
  }

  public long getTotalComputingTime() {
    return totalComputingTime;
  }

  @Override
  public List<S> getResult() {
    if (externalArchive != null) {
      return externalArchive.getSolutionList();
    } else {
      return population;
    }
  }

  public void updatePopulation(List<S> newPopulation) {
    this.population = newPopulation;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return "Evolutionary algorithm";
  }

  public Evaluation<S> getEvaluation() {
    return evaluation;
  }

  @Override
  public Observable<Map<String, Object>> getObservable() {
    return observable;
  }
}
