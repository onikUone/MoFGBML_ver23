package jmetal6.example.operator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jmetal6.lab.plot.PlotFront;
import jmetal6.lab.plot.impl.Plot2DSmile;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.IntegerSBXCrossover;
import jmetal6.problem.integerproblem.IntegerProblem;
import jmetal6.problem.multiobjective.NMMin;
import jmetal6.solution.integersolution.IntegerSolution;
import jmetal6.util.JMetalException;
import jmetal6.util.comparator.IntegerVariableComparator;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * This class is intended to verify the working of the SBX crossover operator for Integer encoding
 *
 * A figure depicting the values obtained when generating 100000 points, a granularity of 200, and a number
 * of different distribution index values (5, 10, 20) can be found here:
 * <a href="https://github.com/jMetal/jMetal/blob/master/figures/integerPolynomialMutation.png">
 * Polynomial mutation (Integer) </a>
 */
public class IntegerSBXCrossoverExample {
  /**
   * Program to generate data representing the distribution of points generated by a SBX
   * crossover operator. The parameters to be introduced by the command line are:
   * - numberOfSolutions: number of solutions to generate
   * - granularity: number of subdivisions to be considered.
   * - distributionIndex: distribution index of the polynomial mutation operator
   * - outputFile: file containing the results
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    if (args.length != 3) {
      throw new JMetalException("Usage: numberOfSolutions granularity distributionIndex") ;
    }
    int numberOfPoints = Integer.valueOf(args[0]) ;
    int granularity = Integer.valueOf(args[1]) ;
    double distributionIndex = Double.valueOf(args[2]) ;
    IntegerProblem problem ;

    problem = new NMMin(1, -50, 50, -100, 100) ;
    CrossoverOperator<IntegerSolution> crossover = new IntegerSBXCrossover(1.0, distributionIndex) ;

    IntegerSolution solution1 = problem.createSolution() ;
    IntegerSolution solution2 = problem.createSolution() ;
    solution1.setVariable(0, -50);
    solution2.setVariable(0, 50);
    List<IntegerSolution> parents = Arrays.asList(solution1, solution2) ;

    List<IntegerSolution> population = new ArrayList<>(numberOfPoints) ;
    for (int i = 0; i < numberOfPoints ; i++) {
      List<IntegerSolution> solutions = (List<IntegerSolution>) crossover.execute(parents);
      population.add(solutions.get(0)) ;
      population.add(solutions.get(1)) ;
    }

    Collections.sort(population, new IntegerVariableComparator()) ;

    double[][] classifier = classify(population, problem, granularity);

    PlotFront plot = new Plot2DSmile(classifier) ;
    plot.plot();
  }

  private static double[][] classify(List<IntegerSolution> solutions, IntegerProblem problem, int granularity) {
    double grain = (problem.getUpperBound(0) - problem.getLowerBound(0)) / granularity ;
    double[][] classifier = new double[granularity][] ;
    for (int i = 0 ; i < granularity; i++) {
      classifier[i] = new double[2] ;
      classifier[i][0] = problem.getLowerBound(0) + i * grain ;
      classifier[i][1] = 0 ;
    }

    for (IntegerSolution solution : solutions) {
      boolean found = false ;
      int index = 0 ;
      while (!found) {
        if (solution.getVariable(0) <= classifier[index][0]) {
          classifier[index][1] ++ ;
          found = true ;
        } else {
          if (index == (granularity - 1)) {
            classifier[index][1] ++ ;
            found = true ;
          } else {
            index++;
          }
        }
      }
    }

    return classifier ;
  }
}