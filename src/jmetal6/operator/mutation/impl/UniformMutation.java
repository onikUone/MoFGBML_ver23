package jmetal6.operator.mutation.impl;

import jmetal6.operator.mutation.MutationOperator;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.solution.util.repairsolution.RepairDoubleSolution;
import jmetal6.solution.util.repairsolution.impl.RepairDoubleSolutionWithBoundValue;
import jmetal6.util.JMetalException;
import jmetal6.util.pseudorandom.JMetalRandom;
import jmetal6.util.pseudorandom.RandomGenerator;

/**
 * This class implements a uniform mutation operator.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class UniformMutation implements MutationOperator<DoubleSolution> {
  private double perturbation;
  private Double mutationProbability = null;
  private RandomGenerator<Double> randomGenerator;
  private RepairDoubleSolution solutionRepair;

  /** Constructor */
  public UniformMutation(double mutationProbability, double perturbation) {
    this(
        mutationProbability,
        perturbation,
        new RepairDoubleSolutionWithBoundValue(),
        () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public UniformMutation(
      double mutationProbability,
      double perturbation,
      RepairDoubleSolution solutionRepair,
      RandomGenerator<Double> randomGenerator) {
    this.mutationProbability = mutationProbability;
    this.perturbation = perturbation;
    this.randomGenerator = randomGenerator;
    this.solutionRepair = solutionRepair;
  }

  /** Constructor */
  public UniformMutation(
      double mutationProbability,
      double perturbation,
      RepairDoubleSolution solutionRepair) {
    this(mutationProbability, perturbation, solutionRepair, () -> JMetalRandom.getInstance().nextDouble()) ;
  }


  /* Getters */
  public double getPerturbation() {
    return perturbation;
  }

  @Override
  public double getMutationProbability() {
    return mutationProbability;
  }

  /* Setters */
  public void setPerturbation(Double perturbation) {
    this.perturbation = perturbation;
  }

  public void setMutationProbability(Double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  /**
   * Perform the operation
   *
   * @param probability Mutation setProbability
   * @param solution The solution to mutate
   */
  public void doMutation(double probability, DoubleSolution solution) {
    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      if (randomGenerator.getRandomValue() < probability) {
        double rand = randomGenerator.getRandomValue();
        double tmp = (rand - 0.5) * perturbation;

        tmp += solution.getVariable(i);

        tmp =
            solutionRepair.repairSolutionVariableValue(
                tmp, solution.getLowerBound(i), solution.getUpperBound(i));

        solution.setVariable(i, tmp);
      }
    }
  }

  /** Execute() method */
  @Override
  public DoubleSolution execute(DoubleSolution solution) {
    if (null == solution) {
      throw new JMetalException("Null parameter");
    }

    doMutation(mutationProbability, solution);

    return solution;
  }
}
