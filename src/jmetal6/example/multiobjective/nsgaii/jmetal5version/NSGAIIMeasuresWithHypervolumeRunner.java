package jmetal6.example.multiobjective.nsgaii.jmetal5version;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIMeasures;
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
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.measure.MeasureListener;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.DurationMeasure;

/**
 * Class to configure and run the NSGA-II algorithm (variant with measures)
 */
public class NSGAIIMeasuresWithHypervolumeRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.nsgaii.NSGAIIMeasuresRunner problemName [referenceFront]
   */
  public static void main(String[] args)
      throws JMetalException, InterruptedException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "referenceFronts/ZDT1.pf" ;
    }

    problem = ProblemUtils.<DoubleSolution> loadProblem(problemName);

    double crossoverProbability = 0.9 ;
    double crossoverDistributionIndex = 20.0 ;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    int maxEvaluations = 25000 ;
    int populationSize = 100 ;

    algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation, populationSize)
        .setSelectionOperator(selection)
        .setMaxEvaluations(maxEvaluations)
        .setVariant(NSGAIIBuilder.NSGAIIVariant.Measures)
        .build() ;

    ((NSGAIIMeasures<DoubleSolution>)algorithm).setReferenceFront(new ArrayFront(referenceParetoFront));

    /* Measure management */
    MeasureManager measureManager = ((NSGAIIMeasures<DoubleSolution>)algorithm).getMeasureManager() ;

    DurationMeasure currentComputingTime =
        (DurationMeasure) measureManager.<Long>getPullMeasure("currentExecutionTime");

    BasicMeasure<Double> hypervolumeMeasure =
            (BasicMeasure<Double>) measureManager.<Double>getPushMeasure("hypervolume");

    hypervolumeMeasure.register(new Listener());
    /* End of measure management */

    Thread algorithmThread = new Thread(algorithm) ;
    algorithmThread.start();

    algorithmThread.join();

    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = currentComputingTime.get() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }

  private static class Listener implements MeasureListener<Double> {
    private static int counter = 0 ;
    @Override synchronized public void measureGenerated(Double value) {
      if ((counter++ % 10 == 0)) {
        System.out.println("Hypervolume: " + value) ;
      }
    }
  }
}
