package jmetal6.auto.util.fitness.impl;

import java.util.List;

import jmetal6.auto.util.fitness.Fitness;
import jmetal6.component.densityestimator.DensityEstimator;
import jmetal6.component.densityestimator.impl.KnnDensityEstimator;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.StrengthRanking;
import jmetal6.solution.Solution;

public class SPEA2Fitness<S extends Solution<?>> implements Fitness<S> {
  private String attributeId = getClass().getName() ;
  Ranking<S> ranking = new StrengthRanking<S>() ;
  DensityEstimator<S> densityEstimator = new KnnDensityEstimator<>(1) ;

  @Override
  public double getFitness(S solution) {
    return (double) solution.getAttribute(attributeId);
  }

  @Override
  public void computeFitness(List<S> solutionList) {
    ranking.computeRanking(solutionList) ;
    densityEstimator.computeDensityEstimator(solutionList);

    for (S solution : solutionList) {
      double fitness = (int)solution.getAttribute(ranking.getAttributeId())
          + 1.0/ (2.0 + (double)solution.getAttribute(densityEstimator.getAttributeId())) ;
      solution.setAttribute(attributeId, fitness);
    }
  }
}
