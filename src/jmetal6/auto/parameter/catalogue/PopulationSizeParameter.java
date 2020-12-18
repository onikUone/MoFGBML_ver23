package jmetal6.auto.parameter.catalogue;

import jmetal6.auto.parameter.Parameter;

public class PopulationSizeParameter extends Parameter<Integer> {

  public PopulationSizeParameter(String args[]) {
    super("populationSize", args) ;
  }

  @Override
  public Parameter<Integer> parse() {
    setValue(on("--populationSize", getArgs(), Integer::parseInt));
    return this ;
  }

  @Override
  public void check() {
    if (getValue() <= 0) {
      throw new RuntimeException("The population size cannot not be <= 0") ;
    }
  }
}
