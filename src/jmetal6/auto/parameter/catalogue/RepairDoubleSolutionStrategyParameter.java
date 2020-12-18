package jmetal6.auto.parameter.catalogue;

import java.util.List;
import java.util.function.Function;

import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.solution.util.repairsolution.RepairDoubleSolution;
import jmetal6.solution.util.repairsolution.impl.RepairDoubleSolutionWithBoundValue;
import jmetal6.solution.util.repairsolution.impl.RepairDoubleSolutionWithOppositeBoundValue;
import jmetal6.solution.util.repairsolution.impl.RepairDoubleSolutionWithRandomValue;

public class RepairDoubleSolutionStrategyParameter extends CategoricalParameter<String> {
  public RepairDoubleSolutionStrategyParameter(String name, String args[], List<String> strategies) {
    super(name, args, strategies) ;
  }

  @Override
  public CategoricalParameter<String> parse() {
    setValue(on("--"+getName(), getArgs(), Function.identity()));
    return this ;
  }

  public RepairDoubleSolution getParameter() {
    RepairDoubleSolution result ;
    switch (getValue()) {
      case "random":
        result = new RepairDoubleSolutionWithRandomValue() ;
        break ;
      case "bounds":
        result = new RepairDoubleSolutionWithBoundValue() ;
        break ;
      case "round":
        result = new RepairDoubleSolutionWithOppositeBoundValue() ;
        break ;
      default:
        throw new RuntimeException("Repair strategy unknown: " + getName()) ;
    }

    return result ;
  }
}
