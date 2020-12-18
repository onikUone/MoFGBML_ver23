package jmetal6.auto.algorithm.spea2;

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
import jmetal6.component.densityestimator.impl.KnnDensityEstimator;
import jmetal6.component.ranking.Ranking;
import jmetal6.component.ranking.impl.StrengthRanking;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.zdt.ZDT1;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.solution.util.repairsolution.RepairDoubleSolution;
import jmetal6.solution.util.repairsolution.impl.RepairDoubleSolutionWithRandomValue;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class implementing an algorithm based on SPEA2 by using the {@link EvolutionaryAlgorithm} class.
 *
 * @author Antonio J. Nebro (ajnebro@uma.es)
 */
public class SPEA2 {
  public static void main(String[] args) {
    DoubleProblem problem = new ZDT1();
    String referenceParetoFront = "/pareto_fronts/ZDT1.pf";

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

    Ranking<DoubleSolution> ranking = new StrengthRanking<>() ;
    DensityEstimator<DoubleSolution> densityEstimator = new KnnDensityEstimator<>(1);

    Preference<DoubleSolution> preferenceForReplacement = new Preference<>(ranking, densityEstimator) ;
    Replacement<DoubleSolution> replacement =
        new RankingAndDensityEstimatorReplacement<>(preferenceForReplacement, Replacement.RemovalPolicy.sequential);

    int tournamentSize = 2 ;
    Preference<DoubleSolution> preferenceForSelection = new Preference<>(ranking, densityEstimator, preferenceForReplacement) ;
    MatingPoolSelection<DoubleSolution> selection =
        new NaryTournamentMatingPoolSelection<>(
            tournamentSize, variation.getMatingPoolSize(), preferenceForSelection);

    EvolutionaryAlgorithm<DoubleSolution> algorithm =
        new EvolutionaryAlgorithm<>(
            "SPEA2",
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
