package jmetal6.example.multiobjective.smpso;

import org.knowm.xchart.BitmapEncoder;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.smpso.SMPSORP;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.archivewithreferencepoint.ArchiveWithReferencePoint;
import jmetal6.util.archivewithreferencepoint.impl.CrowdingDistanceArchiveWithReferencePoint;
import jmetal6.util.chartcontainer.ChartContainerWithReferencePoints;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;
import jmetal6.util.measure.MeasureListener;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SMPSORPWithMultipleReferencePointsAndChartsRunner {
  /**
   * Program to run the SMPSORP algorithm with three reference points and plotting a graph during the algorithm
   * execution. SMPSORP is described in "Extending the Speed-constrained Multi-Objective PSO (SMPSO) With Reference Point Based Preference
   * Articulation. Antonio J. Nebro, Juan J. Durillo, José García-Nieto, Cristóbal Barba-González,
   * Javier Del Ser, Carlos A. Coello Coello, Antonio Benítez-Hidalgo, José F. Aldana-Montes.
   * Parallel Problem Solving from Nature -- PPSN XV. Lecture Notes In Computer Science, Vol. 11101,
   * pp. 298-310. 2018".
   *
   * @author Antonio J. Nebro
   */
  public static void main(String[] args) throws JMetalException, IOException {
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
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1" ;
      referenceParetoFront = "referenceFronts/ZDT1.pf" ;
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

    List<List<Double>> referencePoints;
    referencePoints = new ArrayList<>();
    //referencePoints.add(Arrays.asList(0.6, 0.1)) ;
    referencePoints.add(Arrays.asList(0.2, 0.3)) ;
    referencePoints.add(Arrays.asList(0.8, 0.2)) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    int maxIterations = 250;
    int swarmSize = 100 ;

    List<ArchiveWithReferencePoint<DoubleSolution>> archivesWithReferencePoints = new ArrayList<>();

    for (int i = 0 ; i < referencePoints.size(); i++) {
      archivesWithReferencePoints.add(
          new CrowdingDistanceArchiveWithReferencePoint<DoubleSolution>(
              swarmSize/referencePoints.size(), referencePoints.get(i))) ;
    }

    algorithm = new SMPSORP(problem,
            swarmSize,
            archivesWithReferencePoints,
            referencePoints,
            mutation,
            maxIterations,
            0.0, 1.0,
            0.0, 1.0,
            2.5, 1.5,
            2.5, 1.5,
            0.1, 0.1,
            -1.0, -1.0,
            new SequentialSolutionListEvaluator<>() );

    /* Measure management */
    MeasureManager measureManager = ((SMPSORP) algorithm).getMeasureManager();

    BasicMeasure<List<DoubleSolution>> solutionListMeasure = (BasicMeasure<List<DoubleSolution>>) measureManager
            .<List<DoubleSolution>>getPushMeasure("currentPopulation");
    CountingMeasure iterationMeasure = (CountingMeasure) measureManager.<Long>getPushMeasure("currentIteration");

    ChartContainerWithReferencePoints chart = new ChartContainerWithReferencePoints(algorithm.getName(), 80);
    chart.setFrontChart(0, 1, referenceParetoFront);
    chart.setReferencePoint(referencePoints);
    chart.initChart();

    solutionListMeasure.register(new ChartListener(chart));
    iterationMeasure.register(new IterationListener(chart));

    /* End of measure management */

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    chart.saveChart("SMPSORP", BitmapEncoder.BitmapFormat.PNG);
    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    new SolutionListOutput(population)
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    for (int i = 0 ; i < archivesWithReferencePoints.size(); i++) {
      new SolutionListOutput(archivesWithReferencePoints.get(i).getSolutionList())
          .setVarFileOutputContext(new DefaultFileOutputContext("VAR" + i + ".tsv"))
          .setFunFileOutputContext(new DefaultFileOutputContext("FUN" + i + ".tsv"))
          .print();
    }

    System.exit(0);
  }

  private static class ChartListener implements MeasureListener<List<DoubleSolution>> {
    private ChartContainerWithReferencePoints chart;
    private int iteration = 0;

    public ChartListener(ChartContainerWithReferencePoints chart) {
      this.chart = chart;
      this.chart.getFrontChart().setTitle("Iteration: " + this.iteration);
    }

    private void refreshChart(List<DoubleSolution> solutionList) {
      if (this.chart != null) {
        iteration++;
        this.chart.getFrontChart().setTitle("Iteration: " + this.iteration);
        this.chart.updateFrontCharts(solutionList);
        this.chart.refreshCharts();

        new SolutionListOutput(solutionList)
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR." + iteration + ".tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN." + iteration + ".tsv"))
            .print();
      }
    }

    @Override
    synchronized public void measureGenerated(List<DoubleSolution> solutions) {
      refreshChart(solutions);
    }
  }

  private static class IterationListener implements MeasureListener<Long> {
    ChartContainerWithReferencePoints chart;

    public IterationListener(ChartContainerWithReferencePoints chart) {
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
