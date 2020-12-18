package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.singleobjective.Sphere;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a steady-state genetic algorithm. The target problem is Sphere
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SteadyStateGeneticAlgorithmRunner {
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.SteadyStateGeneticAlgorithmRunner
   */
  public static void main(String[] args) throws Exception {
    Algorithm<DoubleSolution> algorithm;
    DoubleProblem problem = new Sphere(20) ;

    CrossoverOperator<DoubleSolution> crossoverOperator =
        new SBXCrossover(0.9, 20.0) ;
    MutationOperator<DoubleSolution> mutationOperator =
        new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0) ;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selectionOperator = new BinaryTournamentSelection<DoubleSolution>() ;

    algorithm = new GeneticAlgorithmBuilder<DoubleSolution>(problem, crossoverOperator, mutationOperator)
        .setPopulationSize(100)
        .setMaxEvaluations(25000)
        .setSelectionOperator(selectionOperator)
        .setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE)
        .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute() ;

    long computingTime = algorithmRunner.getComputingTime() ;

    DoubleSolution solution = algorithm.getResult() ;
    List<DoubleSolution> population = new ArrayList<>(1) ;
    population.add(solution) ;

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
        .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

    JMetalLogger.logger.info("Fitness: " + solution.getObjective(0)) ;
  }
}
