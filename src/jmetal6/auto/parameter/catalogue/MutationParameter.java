package jmetal6.auto.parameter.catalogue;

import java.util.List;
import java.util.function.Function;

import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.auto.parameter.Parameter;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.mutation.impl.UniformMutation;
import jmetal6.solution.doublesolution.DoubleSolution;

public class MutationParameter extends CategoricalParameter<String> {
  public MutationParameter(String args[], List<String> mutationOperators) {
    super("mutation", args, mutationOperators);
  }

  public CategoricalParameter<String> parse() {
    setValue(on("--mutation", getArgs(), Function.identity()));

    for (Parameter<?> parameter : getGlobalParameters()) {
      parameter.parse().check();
    }

    getSpecificParameters()
        .forEach(
            pair -> {
              if (pair.getKey().equals(getValue())) {
                pair.getValue().parse().check();
              }
            });

    return this;
  }

  public MutationOperator<DoubleSolution> getParameter() {
    MutationOperator<DoubleSolution> result;
    Double mutationProbability = (Double) findGlobalParameter("mutationProbability").getValue();
    RepairDoubleSolutionStrategyParameter repairDoubleSolution =
            (RepairDoubleSolutionStrategyParameter) findGlobalParameter("mutationRepairStrategy");

    switch (getValue()) {
      case "polynomial":
        Double distributionIndex =
                (Double) findSpecificParameter("polynomialMutationDistributionIndex").getValue();
        result =
                new PolynomialMutation(
                        mutationProbability, distributionIndex, repairDoubleSolution.getParameter());
        break;
      case "uniform":
        Double perturbation = (Double) findSpecificParameter("uniformMutationPerturbation").getValue();
        result =
                new UniformMutation(mutationProbability, perturbation, repairDoubleSolution.getParameter());
        break;
      default:
        throw new RuntimeException("Mutation operator does not exist: " + getName());
    }
    return result;
  }

  @Override
  public String getName() {
    return "mutation";
  }
}
