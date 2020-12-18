package jmetal6.example.multiobjective.smpso;

import org.knowm.xchart.BitmapEncoder;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.smpso.SMPSOBuilder;
import jmetal6.algorithm.multiobjective.smpso.SMPSOMeasures;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.archive.BoundedArchive;
import jmetal6.util.archive.impl.CrowdingDistanceArchive;
import jmetal6.util.chartcontainer.ChartContainer;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.measure.MeasureListener;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;

import java.io.IOException;
import java.util.List;

/**
 * Class to configure and run the NSGA-II algorithm (variant with measures)
 */
public class SMPSOMeasuresWithChartsRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIMeasuresRunner problemName [referenceFront]
   */
  public static void main(String[] args)
          throws JMetalException, InterruptedException, IOException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    MutationOperator<DoubleSolution> mutation;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.Golinski";
      referenceParetoFront = "referenceFronts/Golinski.pf" ;
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

    BoundedArchive<DoubleSolution> archive = new CrowdingDistanceArchive<DoubleSolution>(100) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    int maxIterations = 3000 ;
    int swarmSize = 200 ;

    algorithm = new SMPSOBuilder(problem, archive)
        .setMutation(mutation)
        .setMaxIterations(maxIterations)
        .setSwarmSize(swarmSize)
        .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
        .setVariant(SMPSOBuilder.SMPSOVariant.Measures)
        .build();

    /* Measure management */
    MeasureManager measureManager = ((SMPSOMeasures)algorithm).getMeasureManager() ;

    BasicMeasure<List<DoubleSolution>> solutionListMeasure = (BasicMeasure<List<DoubleSolution>>) measureManager
            .<List<DoubleSolution>>getPushMeasure("currentPopulation");
    CountingMeasure iterationMeasure = (CountingMeasure) measureManager.<Long>getPushMeasure("currentIteration");

    ChartContainer chart = new ChartContainer(algorithm.getName(), 80);
    chart.setFrontChart(0, 1, referenceParetoFront);
    //chart.setVarChart(0, 1);
    chart.initChart();

    solutionListMeasure.register(new ChartListener(chart));
    iterationMeasure.register(new IterationListener(chart));

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

  private static class IterationListener implements MeasureListener<Long> {
    ChartContainer chart;

    public IterationListener(ChartContainer chart) {
      this.chart = chart;
      this.chart.getFrontChart().setTitle("Iteration: " + 0);
    }

    @Override
    synchronized public void measureGenerated(Long iteration) {
      if (this.chart != null) {
        this.chart.getFrontChart().setTitle("Iteration: " + iteration);
      }
    }
  }
}
