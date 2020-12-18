package jmetal6.auto.parameter.catalogue;

import java.util.List;
import java.util.function.Function;

import jmetal6.auto.component.initialsolutionscreation.InitialSolutionsCreation;
import jmetal6.auto.component.initialsolutionscreation.impl.LatinHypercubeSamplingSolutionsCreation;
import jmetal6.auto.component.initialsolutionscreation.impl.RandomSolutionsCreation;
import jmetal6.auto.component.initialsolutionscreation.impl.ScatterSearchSolutionsCreation;
import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;

public class CreateInitialSolutionsParameter extends CategoricalParameter<String> {
  private String[] args ;

  public CreateInitialSolutionsParameter(String args[], List<String> validValues) {
    super("createInitialSolutions", args, validValues) ;
    this.args = args ;
  }

  @Override
  public CategoricalParameter<String>  parse() {
    setValue(on("--createInitialSolutions", args, Function.identity()));
    return this ;
  }

  public InitialSolutionsCreation<DoubleSolution> getParameter(DoubleProblem problem, int populationSize) {
    switch (getValue()) {
      case "random":
        return new RandomSolutionsCreation(problem, populationSize);
      case "scatterSearch":
        return new ScatterSearchSolutionsCreation(problem, populationSize, 4);
      case "latinHypercubeSampling":
        return new LatinHypercubeSamplingSolutionsCreation(problem, populationSize);
      default:
        throw new RuntimeException(
            getValue() + " is not a valid initialization strategy");
    }
  }

  @Override
  public String getName() {
    return "createInitialSolutions";
  }
}
