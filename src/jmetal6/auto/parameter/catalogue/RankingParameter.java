package jmetal6.auto.parameter.catalogue;

import java.util.List;
import java.util.function.Function;

import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.auto.parameter.Parameter;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.FastNonDominatedSortRanking;
import jmetal6.component.ranking.impl.StrengthRanking;
import jmetal6.solution.Solution;

public class RankingParameter <S extends Solution<?>> extends CategoricalParameter<String> {
  public RankingParameter(String name, String args[], List<String> validRankings) {
    super(name, args, validRankings);
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

  public Ranking<S> getParameter() {
    Ranking<S> result ;
    switch (getValue()) {
      case "dominanceRanking":
        result = new FastNonDominatedSortRanking<>() ;
        break;
      case "strengthRanking":
        result = new StrengthRanking<>() ;
        break;
      default:
        throw new RuntimeException("Ranking does not exist: " + getName());
    }
    return result;
  }
}
