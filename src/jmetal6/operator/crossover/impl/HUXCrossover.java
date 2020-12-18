package jmetal6.operator.crossover.impl;

import java.util.ArrayList;
import java.util.List;

import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalException;
import jmetal6.util.binarySet.BinarySet;
import jmetal6.util.checking.Check;
import jmetal6.util.pseudorandom.JMetalRandom;
import jmetal6.util.pseudorandom.RandomGenerator;

/**
 * This class allows to apply a HUX crossover operator using two parent solutions. NOTE: the
 * operator is applied to the first encoding.variable of the solutions, and the type of the
 * solutions must be Binary
 *
 * @author Antonio J. Nebro
 * @author Juan J. Durillo
 * @version 1.0
 */
@SuppressWarnings("serial")
public class HUXCrossover implements CrossoverOperator<BinarySolution> {
  private double crossoverProbability;
  private RandomGenerator<Double> randomGenerator;

  /** Constructor */
  public HUXCrossover(double crossoverProbability) {
    this(crossoverProbability, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public HUXCrossover(double crossoverProbability, RandomGenerator<Double> randomGenerator) {
    Check.probabilityIsValid(crossoverProbability) ;

    this.crossoverProbability = crossoverProbability;
    this.randomGenerator = randomGenerator;
  }

  /* Getter */
  @Override
  public double getCrossoverProbability() {
    return crossoverProbability;
  }

  /* Setter */
  public void setCrossoverProbability(double crossoverProbability) {
    this.crossoverProbability = crossoverProbability;
  }

  /** Execute() method */
  public List<BinarySolution> execute(List<BinarySolution> parents) {
    Check.that(parents.size() == 2, "HUXCrossover.execute: operator needs two parents");

    return doCrossover(crossoverProbability, parents.get(0), parents.get(1));
  }

  /**
   * Perform the crossover operation
   *
   * @param probability Crossover setProbability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containing the two offspring
   * @throws jmetal6.util.JMetalException
   */
  public List<BinarySolution> doCrossover(
      double probability, BinarySolution parent1, BinarySolution parent2) throws JMetalException {
    List<BinarySolution> offspring = new ArrayList<>();
    offspring.add((BinarySolution) parent1.copy());
    offspring.add((BinarySolution) parent2.copy());

    if (randomGenerator.getRandomValue() < probability) {
      for (int var = 0; var < parent1.getNumberOfVariables(); var++) {
        BinarySet p1 = parent1.getVariable(var);
        BinarySet p2 = parent2.getVariable(var);

        for (int bit = 0; bit < p1.size(); bit++) {
          if (p1.get(bit) != p2.get(bit)) {
            if (randomGenerator.getRandomValue() < 0.5) {
              offspring.get(0).getVariable(var).set(bit, p2.get(bit));
              offspring.get(1).getVariable(var).set(bit, p1.get(bit));
            }
          }
        }
      }
    }

    return offspring;
  }

  public int getNumberOfRequiredParents() {
    return 2;
  }

  public int getNumberOfGeneratedChildren() {
    return 2;
  }
}
