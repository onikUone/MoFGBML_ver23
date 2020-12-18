package jmetal6.auto.parameter.catalogue;

import java.util.List;
import java.util.function.Function;

import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.auto.parameter.Parameter;
import jmetal6.component.densityestimator.DensityEstimator;
import jmetal6.component.densityestimator.impl.CrowdingDistanceDensityEstimator;
import jmetal6.component.densityestimator.impl.KnnDensityEstimator;
import jmetal6.solution.Solution;

public class DensityEstimatorParameter<S extends Solution<?>> extends CategoricalParameter<String> {
  public DensityEstimatorParameter(String name, String args[], List<String> validDensityEstimators) {
    super(name, args, validDensityEstimators);
  }

  public CategoricalParameter<String> parse() {
    setValue(on("--" + getName(), getArgs(), Function.identity()));

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

  public DensityEstimator<S> getParameter() {
    DensityEstimator<S> result ;
    switch (getValue()) {
      case "crowdingDistance":
        result = new CrowdingDistanceDensityEstimator<>() ;
        break;
      case "knn":
        result = new KnnDensityEstimator<>(1) ;
        break;
      //case "hypervolumeContribution":
        //result = new Hyper
        default:
        throw new RuntimeException("Density estimator does not exist: " + getName());
    }
    return result;
  }
}
