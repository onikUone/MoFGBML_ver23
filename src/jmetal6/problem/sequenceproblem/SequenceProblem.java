package jmetal6.problem.sequenceproblem;

import jmetal6.problem.Problem;
import jmetal6.solution.sequencesolution.SequenceSolution;

public interface SequenceProblem <S extends SequenceSolution<?>> extends Problem<S>  {
    int getLength() ;
}
