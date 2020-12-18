package jmetal6.operator.mutation.impl;

import jmetal6.operator.mutation.MutationOperator;
import jmetal6.solution.sequencesolution.impl.CharSequenceSolution;
import jmetal6.util.checking.Check;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * This class implements a swap mutation. The solution type of the solution must be Permutation.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class CharSequenceRandomMutation implements MutationOperator<CharSequenceSolution> {
  private double mutationProbability;
  private final char[] alphabet;

  /** Constructor */
  public CharSequenceRandomMutation(double mutationProbability, char[] alphabet) {
    Check.probabilityIsValid(mutationProbability);
    this.mutationProbability = mutationProbability;
    this.alphabet = alphabet;
  }

  /* Getters */
  public double getMutationProbability() {
    return mutationProbability;
  }

  /* Setters */
  public void setMutationProbability(double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  /* Execute() method */
  @Override
  public CharSequenceSolution execute(CharSequenceSolution solution) {
    Check.isNotNull(solution);

    doMutation(solution);
    return solution;
  }

  /** Performs the operation */
  public void doMutation(CharSequenceSolution solution) {
    int sequenceLength = solution.getNumberOfVariables();

    for (int i = 0; i < sequenceLength; i++) {
      if (JMetalRandom.getInstance().nextDouble() < mutationProbability) {
        int positionToChange = JMetalRandom.getInstance().nextInt(0, sequenceLength - 1);
        char newCharValue = alphabet[JMetalRandom.getInstance().nextInt(0, alphabet.length - 1)];
        solution.setVariable(positionToChange, newCharValue);
      }
    }
  }
}
