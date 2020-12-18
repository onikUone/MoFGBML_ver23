package jmetal6.algorithm.multiobjective.espea.util;

import jmetal6.solution.Solution;
import jmetal6.util.solutionattribute.impl.GenericSolutionAttribute;

/**
 * Scalarization attribute. A scalarization value is an aggregation of the
 * objective values.
 *
 * @param <S> The solution type
 * @author Marlon Braun <marlon.braun@partner.kit.edu>
 */
@SuppressWarnings("serial")
public class ScalarizationValue<S extends Solution<?>> extends GenericSolutionAttribute<S, Double> {

}
