package jmetal6.util.archive.impl;

import java.util.Collections;
import java.util.Comparator;

import jmetal6.qualityindicator.impl.Hypervolume;
import jmetal6.solution.Solution;
import jmetal6.util.SolutionListUtils;
import jmetal6.util.comparator.HypervolumeContributionComparator;

/**
 * Created by Antonio J. Nebro on 24/09/14.
 */
@SuppressWarnings("serial")
public class HypervolumeArchive<S extends Solution<?>> extends AbstractBoundedArchive<S> {
  private Comparator<S> comparator;
  Hypervolume<S> hypervolume ;

  public HypervolumeArchive(int maxSize, Hypervolume<S> hypervolume) {
    super(maxSize);
    comparator = new HypervolumeContributionComparator<S>() ;
    this.hypervolume = hypervolume ;
  }

  @Override
  public void prune() {
    if (getSolutionList().size() > getMaxSize()) {
      computeDensityEstimator() ;
      S worst = new SolutionListUtils().findWorstSolution(getSolutionList(), comparator) ;
      getSolutionList().remove(worst);
    }
  }

  @Override
  public Comparator<S> getComparator() {
    return comparator ;
  }

  @Override
  public void computeDensityEstimator() {
    hypervolume.computeHypervolumeContribution(archive.getSolutionList(), archive.getSolutionList()) ;
  }

  @Override
  public void sortByDensityEstimator() {
    Collections.sort(getSolutionList(), new HypervolumeContributionComparator<S>());
  }
}
