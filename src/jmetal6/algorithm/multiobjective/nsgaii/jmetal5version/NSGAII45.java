package jmetal6.algorithm.multiobjective.nsgaii.jmetal5version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.impl.AbstractGeneticAlgorithm;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.SolutionListUtils;
import jmetal6.util.comparator.CrowdingDistanceComparator;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.solutionattribute.Ranking;
import jmetal6.util.solutionattribute.impl.CrowdingDistance;
import jmetal6.util.solutionattribute.impl.DominanceRanking;

/**
 * Implementation of NSGA-II following the scheme used in jMetal4.5 and former versions, i.e, without
 * implementing the {@link AbstractGeneticAlgorithm} interface.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class NSGAII45<S extends Solution<?>> implements Algorithm<List<S>> {
  protected List<S> population ;
  protected final int maxEvaluations;
  protected final int populationSize;

  protected final Problem<S> problem;

  protected final SolutionListEvaluator<S> evaluator;

  protected int evaluations;

  protected SelectionOperator<List<S>, S> selectionOperator ;
  protected CrossoverOperator<S> crossoverOperator ;
  protected MutationOperator<S> mutationOperator ;

  /**
   * Constructor
   */
  public NSGAII45(Problem<S> problem, int maxEvaluations, int populationSize,
                  CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                  SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
    super() ;
    this.problem = problem;
    this.maxEvaluations = maxEvaluations;
    this.populationSize = populationSize;

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.selectionOperator = selectionOperator;

    this.evaluator = evaluator;
  }

  /**
   * Run method
   */
  @Override
  public void run() {
    population = createInitialPopulation() ;
    evaluatePopulation(population) ;

    evaluations = populationSize ;

    while (evaluations < maxEvaluations) {
      List<S> offspringPopulation = new ArrayList<>(populationSize);
      for (int i = 0; i < populationSize; i += 2) {
        List<S> parents = new ArrayList<>(2);
        parents.add(selectionOperator.execute(population));
        parents.add(selectionOperator.execute(population));

        List<S> offspring = crossoverOperator.execute(parents);

        mutationOperator.execute(offspring.get(0));
        mutationOperator.execute(offspring.get(1));

        offspringPopulation.add(offspring.get(0));
        offspringPopulation.add(offspring.get(1));
      }

      evaluatePopulation(offspringPopulation) ;

      List<S> jointPopulation = new ArrayList<>();
      jointPopulation.addAll(population);
      jointPopulation.addAll(offspringPopulation);

      Ranking<S> ranking = computeRanking(jointPopulation);

      population = crowdingDistanceSelection(ranking) ;

      evaluations += populationSize ;
    }
  }

  @Override public List<S> getResult() {
    return getNonDominatedSolutions(population);
  }

  protected List<S> createInitialPopulation() {
    List<S> population = new ArrayList<>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      S newIndividual = problem.createSolution();
      population.add(newIndividual);
    }
    return population;
  }

  protected List<S> evaluatePopulation(List<S> population) {
    population = evaluator.evaluate(population, problem);

    return population;
  }

  protected Ranking<S> computeRanking(List<S> solutionList) {
    Ranking<S> ranking = new DominanceRanking<S>();
    ranking.computeRanking(solutionList);

    return ranking;
  }

  protected List<S> crowdingDistanceSelection(Ranking<S> ranking) {
    CrowdingDistance<S> crowdingDistance = new CrowdingDistance<S>();
    List<S> population = new ArrayList<>(populationSize);
    int rankingIndex = 0;
    while (populationIsNotFull(population)) {
      if (subfrontFillsIntoThePopulation(ranking, rankingIndex, population)) {
        addRankedSolutionsToPopulation(ranking, rankingIndex, population);
        rankingIndex++;
      } else {
        crowdingDistance.computeDensityEstimator(ranking.getSubFront(rankingIndex));
        addLastRankedSolutionsToPopulation(ranking, rankingIndex, population);
      }
    }

    return population;
  }

  protected boolean populationIsNotFull(List<S> population) {
    return population.size() < populationSize;
  }

  protected boolean subfrontFillsIntoThePopulation(Ranking<S> ranking, int rank, List<S> population) {
    return ranking.getSubFront(rank).size() < (populationSize - population.size());
  }

  protected void addRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
    List<S> front;

    front = ranking.getSubFront(rank);

    for (S solution : front) {
      population.add(solution);
    }
  }

  protected void addLastRankedSolutionsToPopulation(Ranking<S> ranking, int rank, List<S> population) {
    List<S> currentRankedFront = ranking.getSubFront(rank);

    Collections.sort(currentRankedFront, new CrowdingDistanceComparator<S>());

    int i = 0;
    while (population.size() < populationSize) {
      population.add(currentRankedFront.get(i));
      i++;
    }
  }

  protected List<S> getNonDominatedSolutions(List<S> solutionList) {
    return SolutionListUtils.getNonDominatedSolutions(solutionList);
  }

  @Override public String getName() {
    return "NSGAII45" ;
  }

  @Override public String getDescription() {
    return "Nondominated Sorting Genetic Algorithm version II. Version not using the AbstractGeneticAlgorithm template" ;
  }
}
