package jmetal6.auto.parameter.catalogue;

import java.util.List;
import java.util.function.Function;

import jmetal6.auto.component.variation.Variation;
import jmetal6.auto.component.variation.impl.CrossoverAndMutationVariation;
import jmetal6.auto.component.variation.impl.DifferentialCrossoverVariation;
import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.auto.parameter.Parameter;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.solution.doublesolution.DoubleSolution;

public class VariationParameter extends CategoricalParameter<String> {
  public VariationParameter(String args[], List<String> variationStrategies) {
    super("variation", args, variationStrategies);
  }

  public CategoricalParameter<String> parse() {
    setValue(on("--variation", getArgs(), Function.identity()));

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

  public Variation<?> getParameter() {
    Variation<?> result;
    int offspringPopulationSize = (Integer)findGlobalParameter("offspringPopulationSize").getValue() ;

    switch (getValue()) {
      case "crossoverAndMutationVariation":
        CrossoverParameter crossoverParameter =
            (CrossoverParameter) findSpecificParameter("crossover");
        MutationParameter mutationParameter = (MutationParameter) findSpecificParameter("mutation");

        CrossoverOperator<DoubleSolution> crossoverOperator = crossoverParameter.getParameter();
        MutationOperator<DoubleSolution> mutationOperatorOperator =
            mutationParameter.getParameter();

        result =
            new CrossoverAndMutationVariation<DoubleSolution>(
                offspringPopulationSize, crossoverOperator, mutationOperatorOperator);
        break;
      case "differentialEvolutionVariation":
        DifferentialEvolutionCrossover crossover =
            ((DifferentialEvolutionCrossoverParameter)findGlobalParameter("differentialEvolutionCrossover")).getParameter() ;

        result = new DifferentialCrossoverVariation(offspringPopulationSize, crossover, new PolynomialMutation(0.0, 0.0)) ;
        break ;
      default:
        throw new RuntimeException("Variation component unknown: " + getValue());
    }

    return result;
  }

  @Override
  public String getName() {
    return "variation";
  }
}
