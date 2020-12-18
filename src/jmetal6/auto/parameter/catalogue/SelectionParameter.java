package jmetal6.auto.parameter.catalogue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import jmetal6.auto.component.selection.MatingPoolSelection;
import jmetal6.auto.component.selection.impl.DifferentialEvolutionMatingPoolSelection;
import jmetal6.auto.component.selection.impl.NaryTournamentMatingPoolSelection;
import jmetal6.auto.component.selection.impl.RandomMatingPoolSelection;
import jmetal6.auto.parameter.CategoricalParameter;
import jmetal6.component.densityestimator.DensityEstimator;
import jmetal6.component.densityestimator.impl.CrowdingDistanceDensityEstimator;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.FastNonDominatedSortRanking;
import jmetal6.util.comparator.MultiComparator;

public class SelectionParameter extends CategoricalParameter<String> {
  public SelectionParameter(String args[], List<String> selectionStrategies) {
    super("selection", args, selectionStrategies) ;
  }

  public CategoricalParameter<String> parse() {
    setValue(on("--selection", getArgs(), Function.identity()));

    getSpecificParameters()
        .forEach(
            pair -> {
              if (pair.getKey().equals(getValue())) {
                pair.getValue().parse().check();
              }
            });

    return this;
  }

  public MatingPoolSelection<?> getParameter(int matingPoolSize, Comparator<?> comparator) {
    MatingPoolSelection<?> result ;
    switch(getValue()) {
      case "tournament":
        int tournamentSize =
                (Integer) findSpecificParameter("selectionTournamentSize").getValue();
        /*
        String rankingName = (String) findSpecificParameter("rankingForSelection").getValue() ;
        String densityEstimatorName = (String) findSpecificParameter("densityEstimatorForSelection").getValue() ;
        Ranking<?> ranking ;
        if (rankingName.equals("dominanceRanking")) {
        } else {
          ranking = new StrengthRanking<>() ;
        }

        DensityEstimator<?> densityEstimator ;
        if (densityEstimatorName.equals("crowdingDistance")){
        } else {
          densityEstimator = new KnnDensityEstimator<>(1) ;
        }
        */
        Ranking<?> ranking = new FastNonDominatedSortRanking<>();
        DensityEstimator<?> densityEstimator = new CrowdingDistanceDensityEstimator<>();

        MultiComparator<?> rankingAndCrowdingComparator =
            new MultiComparator(
                Arrays.asList(
                    ranking.getSolutionComparator(), densityEstimator.getSolutionComparator()));
        result = new NaryTournamentMatingPoolSelection(
                tournamentSize, matingPoolSize, rankingAndCrowdingComparator);

        break ;
      case "random":
        result = new RandomMatingPoolSelection<>(matingPoolSize);
        break ;
      case "differentialEvolutionSelection":
        result = new DifferentialEvolutionMatingPoolSelection(matingPoolSize) ;
        break ;
      default:
        throw new RuntimeException("Selection component unknown: " + getValue()) ;
    }

    return result ;
  }
}
