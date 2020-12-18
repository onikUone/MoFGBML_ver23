package jmetal6.auto.algorithm.nsgaii;

import jmetal6.auto.algorithm.EvolutionaryAlgorithm;
import jmetal6.auto.component.evaluation.Evaluation;
import jmetal6.auto.component.evaluation.impl.SequentialEvaluation;
import jmetal6.auto.component.initialsolutionscreation.InitialSolutionsCreation;
import jmetal6.auto.component.initialsolutionscreation.impl.RandomSolutionsCreation;
import jmetal6.auto.component.replacement.Replacement;
import jmetal6.auto.component.replacement.impl.RankingAndDensityEstimatorReplacement;
import jmetal6.auto.component.selection.MatingPoolSelection;
import jmetal6.auto.component.selection.impl.NaryTournamentMatingPoolSelection;
import jmetal6.auto.component.termination.Termination;
import jmetal6.auto.component.termination.impl.TerminationByEvaluations;
import jmetal6.auto.component.variation.Variation;
import jmetal6.auto.component.variation.impl.CrossoverAndMutationVariation;
import jmetal6.auto.util.preference.Preference;
import jmetal6.component.densityestimator.DensityEstimator;
import jmetal6.component.densityestimator.impl.CrowdingDistanceDensityEstimator;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.FastNonDominatedSortRanking;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.zdt.ZDT1;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.solution.util.repairsolution.RepairDoubleSolution;
import jmetal6.solution.util.repairsolution.impl.RepairDoubleSolutionWithRandomValue;
import jmetal6.util.comparator.DominanceComparator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run the NSGA-II using the {@link EvolutionaryAlgorithm} class.
 *
 * @author Antonio J. Nebro (ajnebro@uma.es)
 */
public class NSGAII {
  public static void main(String[] args) {
    DoubleProblem problem = new ZDT1();

    //JMetalRandom.getInstance().setSeed(1);

    int populationSize = 100;
    int offspringPopulationSize = 100;
    int maxNumberOfEvaluations = 25000;

    RepairDoubleSolution crossoverSolutionRepair = new RepairDoubleSolutionWithRandomValue();
    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    CrossoverOperator<DoubleSolution> crossover =
        new SBXCrossover(crossoverProbability, crossoverDistributionIndex, crossoverSolutionRepair);

    RepairDoubleSolution mutationSolutionRepair = new RepairDoubleSolutionWithRandomValue();
    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    MutationOperator<DoubleSolution> mutation =
        new PolynomialMutation(
            mutationProbability, mutationDistributionIndex, mutationSolutionRepair);

    Variation<DoubleSolution> variation =
            new CrossoverAndMutationVariation<>(offspringPopulationSize, crossover, mutation);

    Evaluation<DoubleSolution> evaluation = new SequentialEvaluation<>(problem);

    InitialSolutionsCreation<DoubleSolution> createInitialPopulation =
        new RandomSolutionsCreation<>(problem, populationSize);

    Termination termination = new TerminationByEvaluations(maxNumberOfEvaluations);

    Ranking<DoubleSolution> ranking = new FastNonDominatedSortRanking<>(new DominanceComparator<>());
    //ranking = new ExperimentalFastNonDominanceRanking<>() ;

    DensityEstimator<DoubleSolution> densityEstimator = new CrowdingDistanceDensityEstimator<>();

    Preference<DoubleSolution> preferenceForReplacement = new Preference<>(ranking, densityEstimator) ;
    Replacement<DoubleSolution> replacement =
        new RankingAndDensityEstimatorReplacement<>(preferenceForReplacement, Replacement.RemovalPolicy.oneShot);

    int tournamentSize = 2 ;
    Preference<DoubleSolution> preferenceForSelection = new Preference<>(ranking, densityEstimator, preferenceForReplacement) ;
    MatingPoolSelection<DoubleSolution> selection =
        new NaryTournamentMatingPoolSelection<>(
            tournamentSize, variation.getMatingPoolSize(), preferenceForSelection);

    EvolutionaryAlgorithm<DoubleSolution> algorithm =
        new EvolutionaryAlgorithm<>(
            "NSGA-II",
            evaluation,
            createInitialPopulation,
            termination,
            selection,
            variation,
            replacement);

    //EvaluationObserver evaluationObserver = new EvaluationObserver(1000);
    //RunTimeChartObserver<DoubleSolution> runTimeChartObserver =
        //new RunTimeChartObserver<>("NSGA-II", 80, referenceParetoFront);
    //ExternalArchiveObserver<DoubleSolution> boundedArchiveObserver =
    //    new ExternalArchiveObserver<>(new CrowdingDistanceArchive<>(archiveMaximumSize));

    //algorithm.getObservable().register(evaluationObserver);
    //algorithm.getObservable().register(runTimeChartObserver);
    //evaluation.getObservable().register(boundedArchiveObserver);

    algorithm.run();
    System.out.println("Total computing time: " + algorithm.getTotalComputingTime()) ;
    /*
    algorithm.updatePopulation(boundedArchiveObserver.getArchive().getSolutionList());
    AlgorithmDefaultOutputData.generateMultiObjectiveAlgorithmOutputData(
        algorithm.getResult(), algorithm.getTotalComputingTime());
    */
    new SolutionListOutput(algorithm.getResult())
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();

    System.exit(0);
  }
}
