package jmetal6.algorithm.multiobjective.paes;

import jmetal6.algorithm.AlgorithmBuilder;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class PAESBuilder<S extends Solution<?>>  implements AlgorithmBuilder<PAES<S>> {
  private Problem<S> problem;

  private int archiveSize;
  private int maxEvaluations;
  private int biSections;

  private MutationOperator<S> mutationOperator;

  public PAESBuilder(Problem<S> problem) {
    this.problem = problem;
  }

  public PAESBuilder<S> setArchiveSize(int archiveSize) {
    this.archiveSize = archiveSize;

    return this;
  }

  public PAESBuilder<S> setMaxEvaluations(int maxEvaluations) {
    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public PAESBuilder<S> setBiSections(int biSections) {
    this.biSections = biSections;

    return this;
  }

  public PAESBuilder<S> setMutationOperator(MutationOperator<S> mutation) {
    mutationOperator = mutation;

    return this;
  }

  public PAES<S> build() {
    return new PAES<S>(problem, archiveSize, maxEvaluations, biSections, mutationOperator);
  }

  /*
   * Getters
   */
  public Problem<S> getProblem() {
    return problem;
  }

  public int getArchiveSize() {
    return archiveSize;
  }

  public int getMaxEvaluations() {
    return maxEvaluations;
  }

  public int getBiSections() {
    return biSections;
  }

  public MutationOperator<S> getMutationOperator() {
    return mutationOperator;
  }
}
