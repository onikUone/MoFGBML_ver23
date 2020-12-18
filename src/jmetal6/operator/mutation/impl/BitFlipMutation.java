package jmetal6.operator.mutation.impl;

import jmetal6.operator.mutation.MutationOperator;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalException;
import jmetal6.util.checking.Check;
import jmetal6.util.pseudorandom.JMetalRandom;
import jmetal6.util.pseudorandom.RandomGenerator;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @version 1.0
 *     <p>This class implements a bit flip mutation operator.
 */
@SuppressWarnings("serial")
public class BitFlipMutation implements MutationOperator<BinarySolution> {
  private double mutationProbability;
  private RandomGenerator<Double> randomGenerator;

  /** Constructor */
  public BitFlipMutation(double mutationProbability) {
    this(mutationProbability, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public BitFlipMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {
    if (mutationProbability < 0) {
      throw new JMetalException("Mutation probability is negative: " + mutationProbability);
    }
    this.mutationProbability = mutationProbability;
    this.randomGenerator = randomGenerator;
  }

  /* Getter */
  @Override
  public double getMutationProbability() {
    return mutationProbability;
  }

  /* Setters */
  public void setMutationProbability(double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  /** Execute() method */
  @Override
  public BinarySolution execute(BinarySolution solution) {
    Check.isNotNull(solution);

    doMutation(mutationProbability, solution);
    return solution;
  }

  /**
   * Perform the mutation operation
   *
   * @param probability Mutation setProbability
   * @param solution The solution to mutate
   */
  public void doMutation(double probability, BinarySolution solution) {
    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      for (int j = 0; j < solution.getVariable(i).getBinarySetLength(); j++) {
        if (randomGenerator.getRandomValue() <= probability) {
          solution.getVariable(i).flip(j);
        }
      }
    }
  }
}
