package jmetal6.util.archive.impl;

import java.util.Collections;
import java.util.Comparator;

import jmetal6.solution.Solution;
import jmetal6.util.SolutionListUtils;
import jmetal6.util.comparator.CrowdingDistanceComparator;
import jmetal6.util.solutionattribute.DensityEstimator;
import jmetal6.util.solutionattribute.impl.CrowdingDistance;

/**
 * Created by Antonio J. Nebro on 24/09/14.
 * Modified by Juanjo on 07/04/2015
 */
@SuppressWarnings("serial")
public class CrowdingDistanceArchive<S extends Solution<?>> extends AbstractBoundedArchive<S> {
  private Comparator<S> crowdingDistanceComparator;
  private DensityEstimator<S> crowdingDistance ;

  public CrowdingDistanceArchive(int maxSize) {
    super(maxSize);
    crowdingDistanceComparator = new CrowdingDistanceComparator<S>() ;
    crowdingDistance = new CrowdingDistance<S>() ;
  }

  @Override
  public void prune() {
    if (getSolutionList().size() > getMaxSize()) {
      computeDensityEstimator();
      S worst = new SolutionListUtils().findWorstSolution(getSolutionList(), crowdingDistanceComparator) ;
      getSolutionList().remove(worst);
    }
  }

  @Override
  public Comparator<S> getComparator() {
    return crowdingDistanceComparator ;
  }

  @Override
  public void computeDensityEstimator() {
    crowdingDistance.computeDensityEstimator(getSolutionList());
  }

  @Override
  public void sortByDensityEstimator() {
    Collections.sort(getSolutionList(), new CrowdingDistanceComparator<S>());
  }
}
