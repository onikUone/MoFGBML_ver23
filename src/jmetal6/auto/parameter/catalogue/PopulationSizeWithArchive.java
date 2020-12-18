package jmetal6.auto.parameter.catalogue;

import java.util.List;

import jmetal6.auto.parameter.OrdinalParameter;

public class PopulationSizeWithArchive extends OrdinalParameter<Integer> {
  private String[] args ;

  public PopulationSizeWithArchive(String args[], List<Integer> validValues) {
    super("populationSizeWithArchive", args, validValues) ;
    this.args = args ;
    //check(value) ;
  }

  @Override
  public OrdinalParameter<Integer> parse() {
    setValue(on("--populationSizeWithArchive", args, Integer::parseInt));
    return this ;
  }
}
