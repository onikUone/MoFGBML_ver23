package jmetal6.example.singleobjective;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.crossover.impl.SinglePointCrossover;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.BitFlipMutation;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.binaryproblem.BinaryProblem;
import jmetal6.problem.singleobjective.OneMax;
import jmetal6.solution.binarysolution.BinarySolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a steady-state genetic algorithm. The target problem is TSP
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SteadyStateGeneticAlgorithmBinaryEncodingRunner {
  /**
   * Usage: java org.uma.jmetal.runner.singleobjective.SteadyStateGeneticAlgorithmBinaryEncodingRunner
   */
  public static void main(String[] args) throws Exception {
    BinaryProblem problem;
    Algorithm<BinarySolution> algorithm;
    CrossoverOperator<BinarySolution> crossover;
    MutationOperator<BinarySolution> mutation;
    SelectionOperator<List<BinarySolution>, BinarySolution> selection;

    problem = new OneMax(1024) ;

    crossover = new SinglePointCrossover(0.9) ;

    double mutationProbability = 1.0 / problem.getBitsFromVariable(0) ;
    mutation = new BitFlipMutation(mutationProbability) ;

    selection = new BinaryTournamentSelection<BinarySolution>();

    algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
        .setPopulationSize(50)
        .setMaxEvaluations(25000)
        .setSelectionOperator(selection)
        .setVariant(GeneticAlgorithmBuilder.GeneticAlgorithmVariant.STEADY_STATE)
        .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute() ;

    long computingTime = algorithmRunner.getComputingTime() ;

    BinarySolution solution = algorithm.getResult() ;
    List<BinarySolution> population = new ArrayList<>(1) ;
    population.add(solution) ;

    new SolutionListOutput(population)
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

    JMetalLogger.logger.info("Fitness: " + solution.getObjective(0)) ;
    JMetalLogger.logger.info("Solution: " + solution.getVariable(0)) ;
  }
}
