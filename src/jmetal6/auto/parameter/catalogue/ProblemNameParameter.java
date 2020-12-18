package jmetal6.auto.parameter.catalogue;

import java.util.function.Function;

import jmetal6.auto.parameter.Parameter;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.ProblemUtils;

public class ProblemNameParameter<S extends Solution<?>> extends Parameter<String> {
  public ProblemNameParameter(String args[]) {
    super("problemName", args) ;
  }

  @Override
  public void check() {
    // nothing to check
  }

  public Problem<S> getProblem() {
    return ProblemUtils.<S>loadProblem(getValue());
  }

  @Override
  public Parameter<String> parse() {
    setValue(on("--problemName", getArgs(), Function.identity())) ;

    return this ;
  }
}
