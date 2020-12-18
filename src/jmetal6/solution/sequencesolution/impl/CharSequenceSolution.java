package jmetal6.solution.sequencesolution.impl;

import java.util.HashMap;
import java.util.Map;

import jmetal6.solution.AbstractSolution;
import jmetal6.solution.sequencesolution.SequenceSolution;
import jmetal6.util.pseudorandom.JMetalRandom;

/**
 * Defines an implementation of solution representing sequences of chars.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class CharSequenceSolution extends AbstractSolution<Character> implements SequenceSolution<Character> {
  /** Constructor */
  public CharSequenceSolution(int stringLength, int numberOfObjectives) {
    super(stringLength, numberOfObjectives);

    for (int i = 0; i < stringLength; i++) {
      setVariable(i, ' ');
    }
  }

  /** Copy Constructor */
  public CharSequenceSolution(CharSequenceSolution solution) {
    super(solution.getLength(), solution.getNumberOfObjectives());

    for (int i = 0; i < getNumberOfObjectives(); i++) {
      setObjective(i, solution.getObjective(i));
    }

    for (int i = 0; i < getNumberOfVariables(); i++) {
      setVariable(i, solution.getVariable(i));
    }

    for (int i = 0; i < getNumberOfConstraints(); i++) {
      setConstraint(i, solution.getConstraint(i));
    }

    attributes = new HashMap<Object, Object>(solution.attributes);
  }

  @Override
  public CharSequenceSolution copy() {
    return new CharSequenceSolution(this);
  }

  @Override
  public Map<Object, Object> getAttributes() {
    return attributes;
  }

  @Override
  public int getLength() {
    return getNumberOfVariables();
  }
}
