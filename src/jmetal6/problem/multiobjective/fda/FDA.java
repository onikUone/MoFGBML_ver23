package jmetal6.problem.multiobjective.fda;

import jmetal6.problem.BoundedProblem;
import jmetal6.problem.DynamicProblem;
import jmetal6.problem.doubleproblem.impl.AbstractDoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalLogger;

/** Cristóbal Barba <cbarba@lcc.uma.es> */
public abstract class FDA extends AbstractDoubleProblem
    implements DynamicProblem<DoubleSolution, Integer>, BoundedProblem<Double, DoubleSolution> {
  protected double time;
  private boolean changeStatus = false;

  private int tauT = 5;
  private int nT = 10;

  public FDA() {
  }

  public void update(Integer counter) {
    time = (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
    JMetalLogger.logger.info("Received counter: " + counter + ". Time: " + time);

    setChanged();
  }

  @Override
  public boolean hasChanged() {
    return changeStatus;
  }

  @Override
  public void setChanged() {
    changeStatus = true;
  }

  @Override
  public void clearChanged() {
    changeStatus = false;
  }
}
