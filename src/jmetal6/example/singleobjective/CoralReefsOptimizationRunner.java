package jmetal6.example.singleobjective;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.singleobjective.coralreefsoptimization.CoralReefsOptimizationBuilder;
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
import jmetal6.util.comparator.ObjectiveComparator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class to configure and run a coral reefs optimization algorithm. The target
 * problem is OneMax.
 *
 * @author Inacio Medeiros <inaciogmedeiros@gmail.com>
 */
public class CoralReefsOptimizationRunner {
	/**
	 * Usage: java
	 * org.uma.jmetal.runner.singleobjective.CoralReefsOptimizationRunner
	 */
	public static void main(String[] args) throws Exception {
		Algorithm<List<BinarySolution>> algorithm;
		BinaryProblem problem = new OneMax(512);

		CrossoverOperator<BinarySolution> crossoverOperator = new SinglePointCrossover(
				0.9);
		MutationOperator<BinarySolution> mutationOperator = new BitFlipMutation(
				1.0 / problem.getBitsFromVariable(0));
		SelectionOperator<List<BinarySolution>, BinarySolution> selectionOperator = new BinaryTournamentSelection<BinarySolution>();

		algorithm = new CoralReefsOptimizationBuilder<BinarySolution>(problem,
				selectionOperator, crossoverOperator, mutationOperator)
				.setM(10).setN(10).setRho(0.6).setFbs(0.9).setFbr(0.1)
				.setFa(0.1).setPd(0.1).setAttemptsToSettle(3)
				.setComparator(new ObjectiveComparator<BinarySolution>(0))
				.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(
				algorithm).execute();

		List<BinarySolution> population = algorithm.getResult();
		
		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population)
				.setVarFileOutputContext(
						new DefaultFileOutputContext("VAR.tsv"))
				.setFunFileOutputContext(
						new DefaultFileOutputContext("FUN.tsv")).print();

		JMetalLogger.logger.info("Total execution time: " + computingTime
				+ "ms");
		JMetalLogger.logger
				.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger
				.info("Variables values have been written to file VAR.tsv");

	}
}
