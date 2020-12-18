package jmetal6.component.replacement;

import java.util.List;

import jmetal6.solution.Solution;

@FunctionalInterface
public interface Replacement<S extends Solution<?>> {
  enum RemovalPolicy {sequential, oneShot}
  List<S> replace(List<S> currentList, List<S> offspringList) ;
}
