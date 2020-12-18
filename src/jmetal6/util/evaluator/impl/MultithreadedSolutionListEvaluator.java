package jmetal6.util.evaluator.impl;

import java.util.List;

import jmetal6.problem.Problem;
import jmetal6.util.JMetalLogger;
import jmetal6.util.evaluator.SolutionListEvaluator;

/**
 * @author Antonio J. Nebro
 */
@SuppressWarnings("serial")
public class MultithreadedSolutionListEvaluator<S> implements SolutionListEvaluator<S> {

  private int numberOfThreads;

  public MultithreadedSolutionListEvaluator(int numberOfThreads) {
    if (numberOfThreads == 0) {
      this.numberOfThreads = Runtime.getRuntime().availableProcessors();
    } else {
      this.numberOfThreads = numberOfThreads;
      System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism",
          "" + this.numberOfThreads);
    }
    JMetalLogger.logger.info("Number of cores: " + numberOfThreads);
  }

  @Override
  public List<S> evaluate(List<S> solutionList, Problem<S> problem) {
    solutionList.parallelStream().forEach(s -> problem.evaluate(s));

    return solutionList;
  }

  public int getNumberOfThreads() {
    return numberOfThreads;
  }

  @Override
  public void shutdown() {
    //This method is an intentionally-blank override.
  }
}
