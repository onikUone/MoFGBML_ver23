package jmetal6.solution.sequencesolution;

import jmetal6.solution.Solution;

/**
 * Interface representing a sequence of values of the same type
 *
 * @param <T>
 */
public interface SequenceSolution<T> extends Solution<T> {
  int getLength();
}
