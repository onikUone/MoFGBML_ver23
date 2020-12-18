package jmetal6.example.multiobjective.nsgaii.jmetal5version;

import org.knowm.xchart.BitmapEncoder;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIMeasures;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.Problem;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.chartcontainer.ChartContainer;
import jmetal6.util.comparator.GDominanceComparator;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.evaluator.impl.MultithreadedSolutionListEvaluator;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.measure.MeasureListener;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Class for configuring and running the NSGA-II algorithm (parallel version)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ParallelNSGAIIRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.nsgaii.ParallelNSGAIIRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

    String referenceParetoFront = "";

    String problemName;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "referenceFronts/ZDT1.pf";
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    selection = new BinaryTournamentSelection<DoubleSolution>();

    SolutionListEvaluator<DoubleSolution> evaluator =
        new MultithreadedSolutionListEvaluator<DoubleSolution>(8);

    int populationSize = 100;
    NSGAIIBuilder<DoubleSolution> builder =
        new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation, populationSize)
            .setSelectionOperator(selection)
            .setMaxEvaluations(25000)
            .setSolutionListEvaluator(evaluator);

    algorithm = builder.build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    builder.getSolutionListEvaluator().shutdown();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    evaluator.shutdown();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }

  /**
   * Class to configure and run the NSGA-II algorithm (variant with measures) with the G-Dominance Comparator.
   */
  public static class GNSGAIIMeasuresWithChartsRunner extends AbstractAlgorithmRunner {
    /**
     * @param args Command line arguments.
     * @throws SecurityException Invoking command: java org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIMeasuresRunner
     *                           problemName [referenceFront]
     */
    public static void main(String[] args) throws JMetalException, InterruptedException, IOException {
      Problem<DoubleSolution> problem;
      Algorithm<List<DoubleSolution>> algorithm;
      CrossoverOperator<DoubleSolution> crossover;
      MutationOperator<DoubleSolution> mutation;
      SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
      String referenceParetoFront = "";

      String problemName;
      if (args.length == 2) {
        problemName = args[0];
        referenceParetoFront = args[1];
      } else {
        problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
        referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf";
      }

      problem = ProblemUtils.<DoubleSolution>loadProblem(problemName);

      double crossoverProbability = 0.9;
      double crossoverDistributionIndex = 20.0;
      crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

      double mutationProbability = 1.0 / problem.getNumberOfVariables();
      double mutationDistributionIndex = 20.0;
      mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

      selection = new BinaryTournamentSelection<DoubleSolution>(
              new RankingAndCrowdingDistanceComparator<DoubleSolution>());

      int maxEvaluations = 25000;
      int populationSize = 100;

      List<Double> referencePoint = Arrays.asList(0.5, 0.5) ;

      algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation, populationSize)
              .setSelectionOperator(selection)
              .setMaxEvaluations(maxEvaluations)
              .setVariant(NSGAIIBuilder.NSGAIIVariant.Measures)
              .setDominanceComparator(new GDominanceComparator<>(referencePoint))
              .build();

      ((NSGAIIMeasures<DoubleSolution>) algorithm).setReferenceFront(new ArrayFront(referenceParetoFront));

      MeasureManager measureManager = ((NSGAIIMeasures<DoubleSolution>) algorithm).getMeasureManager();

          /* Measure management */
      BasicMeasure<List<DoubleSolution>> solutionListMeasure = (BasicMeasure<List<DoubleSolution>>) measureManager
              .<List<DoubleSolution>>getPushMeasure("currentPopulation");

      ChartContainer chart = new ChartContainer(algorithm.getName(), 100);
      chart.setFrontChart(0, 1, referenceParetoFront);
      chart.setReferencePoint(referencePoint);
      chart.initChart();

      solutionListMeasure.register(new ChartListener(chart));
      /* End of measure management */

      AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
      chart.saveChart("./chart", BitmapEncoder.BitmapFormat.PNG);

      List<DoubleSolution> population = algorithm.getResult();
      long computingTime = algorithmRunner.getComputingTime();

      JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

      printFinalSolutionSet(population);
      if (!referenceParetoFront.equals("")) {
        printQualityIndicators(population, referenceParetoFront);
      }

      System.exit(0) ;
    }

    private static class ChartListener implements MeasureListener<List<DoubleSolution>> {
      private ChartContainer chart;
      private int iteration = 0;

      public ChartListener(ChartContainer chart) {
        this.chart = chart;
        this.chart.getFrontChart().setTitle("Iteration: " + this.iteration);
      }

      private void refreshChart(List<DoubleSolution> solutionList) {
        if (this.chart != null) {
          iteration++;
          this.chart.getFrontChart().setTitle("Iteration: " + this.iteration);
          this.chart.updateFrontCharts(solutionList);
          this.chart.refreshCharts();
        }
      }

      @Override
      synchronized public void measureGenerated(List<DoubleSolution> solutions) {
        refreshChart(solutions);
      }
    }

  }
}
