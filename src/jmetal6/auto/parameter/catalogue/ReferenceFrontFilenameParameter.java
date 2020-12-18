package jmetal6.auto.parameter.catalogue;

import java.util.function.Function;

import jmetal6.auto.parameter.Parameter;

public class ReferenceFrontFilenameParameter extends Parameter<String> {
  public ReferenceFrontFilenameParameter(String args[]) {
    super("referenceFrontFileName", args);
  }

  @Override
  public void check() {
    // TODO
  }

  @Override
  public Parameter<String> parse() {
    setValue(on("--referenceFrontFileName", getArgs(), Function.identity()));

    return this;
  }
}
