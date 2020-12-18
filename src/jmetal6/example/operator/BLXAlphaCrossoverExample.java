package jmetal6.example.operator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jmetal6.lab.plot.PlotFront;
import jmetal6.lab.plot.impl.Plot2DSmile;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.BLXAlphaCrossover;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.Kursawe;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalException;
import jmetal6.util.comparator.DoubleVariableComparator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 *     <p>This class is intended to verify the working of the BLX-alpha crossover operator. A figure
 *     depicting the values obtained when generating 1000000 solutions, a granularity of 100, and a
 *     number of different distribution alpha values (0.1, 0.5) can be found here: <a
 *     href="https://github.com/jMetal/jMetal/blob/master/figures/blxalpha.png">
 *     https://github.com/jMetal/jMetal/blob/master/figures/blxalpha.png</a>
 */
public class BLXAlphaCrossoverExample {
  /**
   * Program to generate data representing the distribution of points generated by a SBX crossover
   * operator. The parameters to be introduced by the command line are: - numberOfSolutions: number
   * of solutions to generate - granularity: number of subdivisions to be considered. - alpha: alpha
   * value - outputFile: file containing the results
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    if (args.length != 3) {
      throw new JMetalException("Usage: numberOfSolutions granularity alpha");
    }
    int numberOfPoints = Integer.valueOf(args[0]);
    int granularity = Integer.valueOf(args[1]);
    double alpha = Double.valueOf(args[2]);
    DoubleProblem problem;

    problem = new Kursawe(1);
    CrossoverOperator<DoubleSolution> crossover = new BLXAlphaCrossover(1.0, alpha);

    DoubleSolution solution1 = problem.createSolution();
    DoubleSolution solution2 = problem.createSolution();
    solution1.setVariable(0, -2.0);
    solution2.setVariable(0, 2.0);
    List<DoubleSolution> parents = Arrays.asList(solution1, solution2);

    List<DoubleSolution> population = new ArrayList<>(numberOfPoints);
    for (int i = 0; i < numberOfPoints; i++) {
      List<DoubleSolution> solutions = (List<DoubleSolution>) crossover.execute(parents);
      population.add(solutions.get(0));
      population.add(solutions.get(1));
    }

    Collections.sort(population, new DoubleVariableComparator());

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("solutionsBLXAlpha"))
        .print();

    double[][] classifier = classify(population, problem, granularity);
    PlotFront plot = new Plot2DSmile(classifier);
    plot.plot();
  }

  private static double[][] classify(
      List<DoubleSolution> solutions, DoubleProblem problem, int granularity) {
    double grain = (problem.getUpperBound(0) - problem.getLowerBound(0)) / granularity;
    double[][] classifier = new double[granularity][];
    for (int i = 0; i < granularity; i++) {
      classifier[i] = new double[2];
      classifier[i][0] = problem.getLowerBound(0) + i * grain;
      classifier[i][1] = 0;
    }

    for (DoubleSolution solution : solutions) {
      boolean found = false;
      int index = 0;
      while (!found) {
        if (solution.getVariable(0) <= classifier[index][0]) {
          classifier[index][1]++;
          found = true;
        } else {
          if (index == (granularity - 1)) {
            classifier[index][1]++;
            found = true;
          } else {
            index++;
          }
        }
      }
    }

    return classifier;
  }
}
