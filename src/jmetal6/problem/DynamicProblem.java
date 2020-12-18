package jmetal6.problem;

import jmetal6.solution.Solution;
import jmetal6.util.observable.Observable;
import jmetal6.util.observer.Observer;

/**
 * Interface representing dynamic problems. Problems implementing this interface act as {@link Observer} entities
 * in the sense that the can register themselves in {@link Observable} objects that may modify some of the problem
 * properties. A dynamic problem has an internal state about whether it has been updated or not, and there are methods
 * to query, set, and reset that state.
 *
 * @author Antonio J. Nebro <ajnebro@uma.es>
 */
public interface DynamicProblem<S extends Solution<?>, D>
    extends Problem<S> {

  void update(D value) ;

  boolean hasChanged() ;
  void setChanged() ;
  void clearChanged() ;
}