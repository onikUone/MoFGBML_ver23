//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal6.example.multiobjective;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.cdg.CDGBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.glt.GLT4;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;

/**
 * Class for configuring and running the CDG algorithm The paper and Matlab code can be download at
 * http://xinyecai.github.io/
 *
 * @author Feng Zhang
 */
public class CDGRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws ClassNotFoundException
   * @throws SecurityException Invoking command: java org.uma.jmetal.runner.multiobjective.CDGRunner
   *     problemName [referenceFront]
   */
  public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    DifferentialEvolutionCrossover crossover;

    String problemName;
    String referenceParetoFront = "";
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "(none)";
    }
    System.out.println("Warning: the problem name is not used anymore and may be removed later.");
    System.out.println("Warning: current problem name: " + problemName);

    problem = new GLT4(10);

    double cr = 1.0;
    double f = 0.5;
    crossover =
        new DifferentialEvolutionCrossover(
            cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN);

    algorithm =
        new CDGBuilder(problem)
            .setCrossover(crossover)
            .setMaxEvaluations(300 * 1000)
            .setPopulationSize(300)
            .setResultPopulationSize(300)
            .setNeighborhoodSelectionProbability(0.9)
            .build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }
}
