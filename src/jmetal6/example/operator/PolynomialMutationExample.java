package jmetal6.example.operator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jmetal6.lab.plot.PlotFront;
import jmetal6.lab.plot.impl.Plot2DSmile;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.singleobjective.Sphere;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalException;
import jmetal6.util.comparator.DoubleVariableComparator;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * This class is intended to verify the working of the polynomial mutation operator. A figure
 * depicting the values obtained when generating 100000 points, a granularity of 200, and a number
 * of different distribution index values (5, 10, 20) can be found here:
 * <a href="https://github.com/jMetal/jMetal/blob/master/figures/polynomialMutation.png">
   Polynomial mutation</a>
 */
public class PolynomialMutationExample {
  /**
   * Program to generate data representing the distribution of points generated by a polynomial
   * mutation operator. The parameters to be introduced by the command line are:
   * - numberOfSolutions: number of solutions to generate
   * - granularity: number of subdivisions to be considered.
   * - distributionIndex: distribution index of the polynomial mutation operator
   * - outputFile: file containing the results
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    if (args.length !=3) {
      throw new JMetalException("Usage: numberOfSolutions granularity distributionIndex") ;
    }
    int numberOfPoints = Integer.valueOf(args[0]) ;
    int granularity = Integer.valueOf(args[1]) ;
    double distributionIndex = Double.valueOf(args[2]) ;
    DoubleProblem problem ;

    problem = new Sphere(1) ;
    MutationOperator<DoubleSolution> mutation = new PolynomialMutation(1.0, distributionIndex) ;

    DoubleSolution solution = problem.createSolution() ;
    solution.setVariable(0, 0.0);

    List<DoubleSolution> population = new ArrayList<>(numberOfPoints) ;
    for (int i = 0 ; i < numberOfPoints ; i++) {
      DoubleSolution newSolution = (DoubleSolution) solution.copy();
      mutation.execute(newSolution) ;
      population.add(newSolution) ;
    }

    Collections.sort(population, new DoubleVariableComparator()) ;
    double[][] classifier = classify(population, problem, granularity);

    PlotFront plot = new Plot2DSmile(classifier) ;
    plot.plot();
  }

  private static double[][] classify(List<DoubleSolution> solutions, DoubleProblem problem, int granularity) {
    double grain = (problem.getUpperBound(0) - problem.getLowerBound(0)) / granularity ;
    double[][] classifier = new double[granularity][] ;
    for (int i = 0 ; i < granularity; i++) {
      classifier[i] = new double[2] ;
      classifier[i][0] = problem.getLowerBound(0) + i * grain ;
      classifier[i][1] = 0 ;
    }

    for (DoubleSolution solution : solutions) {
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
