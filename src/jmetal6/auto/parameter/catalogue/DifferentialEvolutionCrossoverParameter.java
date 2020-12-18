package jmetal6.auto.parameter.catalogue;

import java.util.Arrays;
import java.util.function.Function;

import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.auto.parameter.Parameter;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;

public class DifferentialEvolutionCrossoverParameter extends CategoricalParameter<String> {
  public DifferentialEvolutionCrossoverParameter(String args[]) {
    super("differentialEvolutionCrossover", args, Arrays.asList("DE"));
  }

  @Override
  public void check() {
  }

  public Parameter<String> parse() {
    setValue(on("--differentialEvolutionCrossover", getArgs(), Function.identity()));

    for (Parameter<?> parameter : getGlobalParameters()) {
      parameter.parse().check();
    }

    return this;
  }

  public DifferentialEvolutionCrossover getParameter() {
    DifferentialEvolutionCrossover result;
    Double cr = (Double) findSpecificParameter("cr").getValue();
    Double f  = (Double) findSpecificParameter("f").getValue() ;

    result = new DifferentialEvolutionCrossover(cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN) ;

    return result;
  }

  @Override
  public String getName() {
    return "differentialEvolutionCrossover";
  }
}
