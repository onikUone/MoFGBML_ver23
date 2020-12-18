package jmetal6.auto.parameter.catalogue;

import java.util.List;

import jmetal6.auto.parameter.OrdinalParameter;

public class OffspringPopulationSizeParameter extends OrdinalParameter<Integer> {
  private String[] args ;

  public OffspringPopulationSizeParameter(String args[], List<Integer> validValues) {
    super("offspringPopulationSize", args, validValues) ;
    this.args = args ;
  }

  @Override
  public OrdinalParameter<Integer>  parse() {
    setValue(on("--offspringPopulationSize", args, Integer::parseInt));

    return this ;
  }
}
