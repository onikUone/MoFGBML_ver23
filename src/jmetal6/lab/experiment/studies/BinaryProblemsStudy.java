//

//

package jmetal6.lab.experiment.studies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.mocell.MOCellBuilder;
import jmetal6.algorithm.multiobjective.mochc.MOCHCBuilder;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.algorithm.multiobjective.spea2.SPEA2Builder;
import jmetal6.lab.experiment.Experiment;
import jmetal6.lab.experiment.ExperimentBuilder;
import jmetal6.lab.experiment.component.*;
import jmetal6.lab.experiment.util.ExperimentAlgorithm;
import jmetal6.lab.experiment.util.ExperimentProblem;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.HUXCrossover;
import jmetal6.operator.crossover.impl.SinglePointCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.RandomSelection;
import jmetal6.operator.selection.impl.RankingAndCrowdingSelection;
import jmetal6.problem.Problem;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.problem.multiobjective.OneZeroMax;
import jmetal6.problem.multiobjective.zdt.ZDT5;
import jmetal6.qualityindicator.impl.*;
import jmetal6.qualityindicator.impl.hypervolume.PISAHypervolume;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalException;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Example of experimental study based on solving two binary problems with four algorithms: NSGAII,
 * SPEA2, MOCell, and MOCHC
 *
 * This org.uma.jmetal.experiment assumes that the reference Pareto front are not known, so the must be produced.
 *
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the org.uma.jmetal.experiment are: 1. Configure the org.uma.jmetal.experiment 2. Execute the algorithms
 * 3. Generate the reference Pareto fronts 4. Compute que quality indicators 5. Generate Latex
 * tables reporting means and medians 6. Generate Latex tables with the result of applying the
 * Wilcoxon Rank Sum Test 7. Generate Latex tables with the ranking obtained by applying the
 * Friedman test 8. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class BinaryProblemsStudy {

  private static final int INDEPENDENT_RUNS = 25;

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      throw new JMetalException("Needed arguments: experimentBaseDirectory");
    }
    String experimentBaseDirectory = args[0];

    List<ExperimentProblem<BinarySolution>> problemList = new ArrayList<>();
    problemList.add(new ExperimentProblem<>(new ZDT5()));
    problemList.add(new ExperimentProblem<>(new OneZeroMax(512)));

    List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> algorithmList =
        configureAlgorithmList(problemList);

    Experiment<BinarySolution, List<BinarySolution>> experiment;
    experiment = new ExperimentBuilder<BinarySolution, List<BinarySolution>>("BinaryProblemsStudy")
        .setAlgorithmList(algorithmList)
        .setProblemList(problemList)
        .setExperimentBaseDirectory(experimentBaseDirectory)
        .setOutputParetoFrontFileName("FUN")
        .setOutputParetoSetFileName("VAR")
        .setReferenceFrontDirectory(experimentBaseDirectory + "/BinaryProblemsStudy/referenceFronts")
        .setIndicatorList(Arrays.asList(
            new Epsilon<BinarySolution>(),
            new Spread<BinarySolution>(),
            new GenerationalDistance<BinarySolution>(),
            new PISAHypervolume<BinarySolution>(),
            new InvertedGenerationalDistance<BinarySolution>(),
            new InvertedGenerationalDistancePlus<BinarySolution>())
        )
        .setIndependentRuns(INDEPENDENT_RUNS)
        .setNumberOfCores(8)
        .build();

    new ExecuteAlgorithms<>(experiment).run();
    new GenerateReferenceParetoFront(experiment).run();
    new ComputeQualityIndicators<>(experiment).run();
    new GenerateLatexTablesWithStatistics(experiment).run();
    new GenerateWilcoxonTestTablesWithR<>(experiment).run();
    new GenerateFriedmanTestTables<>(experiment).run();
    new GenerateBoxplotsWithR<>(experiment).setRows(1).setColumns(2).setDisplayNotch().run();
  }

  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of
   * a {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}.
   */

  static List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> configureAlgorithmList(
      List<ExperimentProblem<BinarySolution>> problemList) {
    List<ExperimentAlgorithm<BinarySolution, List<BinarySolution>>> algorithms = new ArrayList<>();
    for (int run = 0; run < INDEPENDENT_RUNS; run++) {

      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BinarySolution>> algorithm = new NSGAIIBuilder<BinarySolution>(
            problemList.get(i).getProblem(),
            new SinglePointCrossover(1.0),
            new BitFlipMutation(
                1.0 / ((BinaryProblem) problemList.get(i).getProblem()).getBitsFromVariable(0)),
                100)
            .setMaxEvaluations(25000)
            .build();
        algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
      }

      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BinarySolution>> algorithm = new SPEA2Builder<BinarySolution>(
            problemList.get(i).getProblem(),
            new SinglePointCrossover(1.0),
            new BitFlipMutation(
                1.0 / ((BinaryProblem) problemList.get(i).getProblem()).getBitsFromVariable(0)))
            .setMaxIterations(250)
            .setPopulationSize(100)
            .build();
        algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
      }

      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BinarySolution>> algorithm = new MOCellBuilder<BinarySolution>(
            problemList.get(i).getProblem(),
            new SinglePointCrossover(1.0),
            new BitFlipMutation(
                1.0 / ((BinaryProblem) problemList.get(i).getProblem()).getBitsFromVariable(0)))
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .build();
        algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
      }

      for (int i = 0; i < problemList.size(); i++) {
        CrossoverOperator<BinarySolution> crossoverOperator;
        MutationOperator<BinarySolution> mutationOperator;
        SelectionOperator<List<BinarySolution>, BinarySolution> parentsSelection;
        SelectionOperator<List<BinarySolution>, List<BinarySolution>> newGenerationSelection;

        crossoverOperator = new HUXCrossover(1.0);
        parentsSelection = new RandomSelection<BinarySolution>();
        newGenerationSelection = new RankingAndCrowdingSelection<BinarySolution>(100);
        mutationOperator = new BitFlipMutation(0.35);
        Algorithm<List<BinarySolution>> algorithm = new MOCHCBuilder(
            (BinaryProblem) problemList.get(i).getProblem())
            .setInitialConvergenceCount(0.25)
            .setConvergenceValue(3)
            .setPreservedPopulation(0.05)
            .setPopulationSize(100)
            .setMaxEvaluations(25000)
            .setCrossover(crossoverOperator)
            .setNewGenerationSelection(newGenerationSelection)
            .setCataclysmicMutation(mutationOperator)
            .setParentSelection(parentsSelection)
            .setEvaluator(new SequentialSolutionListEvaluator<BinarySolution>())
            .build();
        algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i), run));
      }
    }
    return algorithms;
  }
}
