package jmetal6.example.multiobjective.moead.jmetal5version;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.moead.jmetal5version.AbstractMOEAD;
import jmetal6.algorithm.multiobjective.moead.jmetal5version.MOEADBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;

/**
 * Class for configuring and running the MOEA/DD algorithm
 *
 * @author
 */
public class MOEADDRunner extends AbstractAlgorithmRunner {

  /**
   * @param args Command line arguments.
   * @throws SecurityException Invoking command: java
   * org.uma.jmetal.runner.multiobjective.moead.MOEADRunner problemName
   * [referenceFront]
   */
  public static void main(String[] args) throws FileNotFoundException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    String problemName;
    String referenceParetoFront = "";
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.UF.UF2";
      referenceParetoFront = "referenceFronts/UF2.pf";
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

        /**/
    MutationOperator<DoubleSolution> mutation;
    CrossoverOperator<DoubleSolution> crossover;

    double crossoverProbability = 1.0;
    double crossoverDistributionIndex = 30.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    MOEADBuilder builder =  new MOEADBuilder(problem, MOEADBuilder.Variant.MOEADD);
    builder.setCrossover(crossover)
            .setMutation(mutation)
            .setMaxEvaluations(150000)
            .setPopulationSize(300)
            .setResultPopulationSize(300)
            .setNeighborhoodSelectionProbability(0.9)
            .setMaximumNumberOfReplacedSolutions(1)
            .setNeighborSize(20)
            .setFunctionType(AbstractMOEAD.FunctionType.PBI)
            .setDataDirectory("MOEAD_Weights");
    algorithm = builder.build();

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }
}
