package jmetal6.auto.algorithm.nsgaii;

import jmetal6.auto.algorithm.EvolutionaryAlgorithm;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.observer.impl.EvaluationObserver;
import jmetal6.util.observer.impl.RunTimeChartObserver;

/**
 * Class configuring NSGA-II using arguments in the form <key, value> and the {@link AutoNSGAII}
 * class.
 *
 * @author Antonio J. Nebro (ajnebro@uma.es)
 */
public class NSGAIIWithParameters {

  public static void main(String[] args) {
    String[] parameters =
        ("--problemName org.uma.jmetal.problem.multiobjective.zdt.ZDT1 "
                + "--referenceFrontFileName ZDT1.pf "
                + "--maximumNumberOfEvaluations 25000 "
                + "--algorithmResult population "
                + "--populationSize 100 "
                + "--offspringPopulationSize 100 "
                + "--createInitialSolutions random "
                + "--variation crossoverAndMutationVariation "
                + "--selection tournament "
                + "--selectionTournamentSize 2 "
                + "--rankingForSelection dominanceRanking "
                + "--densityEstimatorForSelection crowdingDistance "
                + "--crossover SBX "
                + "--crossoverProbability 0.9 "
                + "--crossoverRepairStrategy bounds "
                + "--sbxDistributionIndex 20.0 "
                + "--mutation polynomial "
                + "--mutationProbability 0.01 "
                + "--mutationRepairStrategy bounds "
                + "--polynomialMutationDistributionIndex 20.0 ")
            .split("\\s+");

    AutoNSGAII NSGAII = new AutoNSGAII();
    NSGAII.parseAndCheckParameters(parameters);

    NSGAII.print(NSGAII.fixedParameterList);
    NSGAII.print(NSGAII.autoConfigurableParameterList);

    EvolutionaryAlgorithm<DoubleSolution> nsgaII = NSGAII.create();

    EvaluationObserver evaluationObserver = new EvaluationObserver(1000);
    RunTimeChartObserver<DoubleSolution> runTimeChartObserver =
        new RunTimeChartObserver<>(
            "NSGA-II", 80, "/pareto_fronts/ZDT1.pf");
    //WriteSolutionsToFilesObserver writeSolutionsToFilesObserver = new WriteSolutionsToFilesObserver() ;

    nsgaII.getObservable().register(evaluationObserver);
    nsgaII.getObservable().register(runTimeChartObserver);
    //nsgaII.getObservable().register(writeSolutionsToFilesObserver);

    nsgaII.run();

    System.out.println("Total computing time: " + nsgaII.getTotalComputingTime()) ;

    new SolutionListOutput(nsgaII.getResult())
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();
  }
}
