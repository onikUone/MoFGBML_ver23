package jmetal6.problem.sequenceproblem.impl;

import jmetal6.problem.AbstractGenericProblem;
import jmetal6.problem.sequenceproblem.SequenceProblem;
import jmetal6.solution.sequencesolution.impl.CharSequenceSolution;

@SuppressWarnings("serial")
public abstract class CharSequenceProblem
    extends AbstractGenericProblem<CharSequenceSolution> implements
        SequenceProblem<CharSequenceSolution> {

  @Override
  public CharSequenceSolution createSolution() {
    return new CharSequenceSolution(getLength(), getNumberOfObjectives()) ;
  }
}
