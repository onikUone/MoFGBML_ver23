package jmetal6.example.multiobjective.wasfga;

import org.knowm.xchart.BitmapEncoder.BitmapFormat;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.wasfga.WASFGAMeasures;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.chartcontainer.ChartContainer;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.measure.MeasureListener;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WASFGAMeasuresRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
 * @throws IOException 
   */
  public static void main(String[] args) throws JMetalException, IOException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
    String referenceParetoFront = "" ;
    List<Double> referencePoint = null;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "referenceFronts/ZDT1.pf" ;
    }

    problem = ProblemUtils.<DoubleSolution> loadProblem(problemName);

    referencePoint = new ArrayList<>();
    referencePoint.add(0.6);
    referencePoint.add(0.4);

    double crossoverProbability = 0.9 ;
    double crossoverDistributionIndex = 20.0 ;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    double epsilon = 0.01 ;
    algorithm = new WASFGAMeasures<DoubleSolution>(
    				problem,
						100,
						250,
						crossover, mutation, selection,new SequentialSolutionListEvaluator<DoubleSolution>(),
            epsilon,
            referencePoint) ;
    
    /* Measure management */
    MeasureManager measureManager = ((WASFGAMeasures<DoubleSolution>) algorithm).getMeasureManager();

    BasicMeasure<List<DoubleSolution>> solutionListMeasure = (BasicMeasure<List<DoubleSolution>>) measureManager
            .<List<DoubleSolution>>getPushMeasure("currentPopulation");
    CountingMeasure iterationMeasure = (CountingMeasure) measureManager.<Long>getPushMeasure("currentEvaluation");

    ChartContainer chart = new ChartContainer(algorithm.getName(), 200);
    chart.setFrontChart(0, 1, referenceParetoFront);
    chart.setReferencePoint(referencePoint);
    chart.setVarChart(0, 1);
    chart.initChart();

    solutionListMeasure.register(new ChartListener(chart));
    iterationMeasure.register(new IterationListener(chart));

    /* End of measure management */
    
    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    chart.saveChart("WASFGA", BitmapFormat.PNG);
    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
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
