package jmetal6.auto.parameter.catalogue;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import jmetal6.auto.component.replacement.Replacement;
import jmetal6.auto.component.replacement.impl.RankingAndDensityEstimatorReplacement;
import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.auto.parameter.Parameter;
import jmetal6.component.densityestimator.DensityEstimator;
import jmetal6.component.densityestimator.impl.CrowdingDistanceDensityEstimator;
import jmetal6.component.densityestimator.impl.KnnDensityEstimator;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.FastNonDominatedSortRanking;
import jmetal6.component.ranking.impl.StrengthRanking;
import jmetal6.solution.doublesolution.DoubleSolution;

public class ReplacementParameter extends CategoricalParameter<String> {
  public ReplacementParameter(String args[], List<String> selectionStrategies) {
    super("replacement", args, selectionStrategies);
  }

  public CategoricalParameter<String> parse() {
    setValue(on("--replacement", getArgs(), Function.identity()));

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

  public Replacement<?> getParameter(Comparator<DoubleSolution> comparator) {
    String removalPolicy = (String) findGlobalParameter("removalPolicy").getValue();
    Replacement<?> result;
    switch (getValue()) {
      case "rankingAndDensityEstimatorReplacement":
        String rankingName = (String) findSpecificParameter("rankingForReplacement").getValue();
        String densityEstimatorName =
            (String) findSpecificParameter("densityEstimatorForReplacement").getValue();

        Ranking<?> ranking;
        if (rankingName.equals("dominanceRanking")) {
          ranking = new FastNonDominatedSortRanking<>();
        } else {
          ranking = new StrengthRanking<>();
        }

        DensityEstimator<?> densityEstimator;
        if (densityEstimatorName.equals("crowdingDistance")) {
          densityEstimator = new CrowdingDistanceDensityEstimator<>();
        } else {
          densityEstimator = new KnnDensityEstimator<>(1);
        }

        if (removalPolicy.equals("oneShot")) {
          result =
              new RankingAndDensityEstimatorReplacement(
                  ranking, densityEstimator, Replacement.RemovalPolicy.oneShot);
        } else {
          result =
              new RankingAndDensityEstimatorReplacement(
                  ranking, densityEstimator, Replacement.RemovalPolicy.sequential);
        }

        break;
      default:
        throw new RuntimeException("Replacement component unknown: " + getValue());
    }

    return result;
  }
}
