package jmetal6.solution.util.attribute;

import java.util.Comparator;

public interface Attribute<S> {
  String getAttributeId() ;
  Comparator<S> getSolutionComparator() ;
}
