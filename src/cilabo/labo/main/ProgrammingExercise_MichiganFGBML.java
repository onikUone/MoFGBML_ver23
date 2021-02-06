package cilabo.labo.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.component.termination.impl.TerminationByEvaluations;
import org.uma.jmetal.component.variation.Variation;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.observer.impl.EvaluationObserver;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.RuleBasedClassifier;
import cilabo.fuzzy.classifier.operator.postProcessing.PostProcessing;
import cilabo.fuzzy.classifier.operator.postProcessing.factory.RemoveNotWinnerProcessing;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.MoFGBML_Learning;
import cilabo.gbml.algorithm.MichiganFGBML;
import cilabo.gbml.operator.crossover.UniformCrossover;
import cilabo.gbml.operator.mutation.MichiganMutation;
import cilabo.gbml.operator.variation.MichiganSolutionVariation;
import cilabo.gbml.problem.impl.ProblemMichiganFGBML;
import cilabo.gbml.solution.MichiganSolution;
import cilabo.metric.ErrorRate;
import cilabo.metric.Metric;
import cilabo.utility.Input;
import cilabo.utility.Parallel;

/**
 * For student belonging in CILAB,
 * this example is implementation of the programming exercise 6.
 */
public class ProgrammingExercise_MichiganFGBML {

	public static void main(String[] args) throws JMetalException, FileNotFoundException {
		String sep = File.separator;
		Parallel.getInstance().initLearningForkJoinPool(1);;

		// 3.2. 数値実験準備
		deterministicTest();

		// 2.9 Michigan-type Fuzzy GBML
		// Load "Pima" dataset
		String dataName = "dataset" + sep + "pima" + sep + "a0_0_pima-10tra.dat";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		// Parameters
		int populationSize = 30;
		int offspringPopulationSize = 10;
		// Problem
		int seed = 0;
		JMetalRandom.getInstance().setSeed(seed);
		ProblemMichiganFGBML<IntegerSolution> problem = new ProblemMichiganFGBML<>(seed, train);
		// Crossover
		double crossoverProbability = 0.9;
		CrossoverOperator<IntegerSolution> crossover = new UniformCrossover(crossoverProbability);
		// Mutation
		double mutationProbability = 1.0 / (double)train.getNdim();
		MutationOperator<IntegerSolution> mutation = new MichiganMutation(mutationProbability,
																	  problem.getKnowledge(),
																	  train);
		// Termination
		int generation = 10000;
		int evaluations = populationSize + generation*offspringPopulationSize;
		Termination termination = new TerminationByEvaluations(evaluations);
		// Variation
		Variation<IntegerSolution> variation = new MichiganSolutionVariation<>(
													offspringPopulationSize, crossover, mutation,
													problem.getKnowledge(),
													problem.getConsequentFactory());
		// Algorithm
		MichiganFGBML<IntegerSolution> algorithm
				= new MichiganFGBML<>(problem, populationSize, offspringPopulationSize,
									crossover, mutation, termination, variation);

		EvaluationObserver evaluationObserver = new EvaluationObserver(10);
		algorithm.getObservable().register(evaluationObserver);

		algorithm.run();

		// Result
		List<RuleBasedClassifier> totalClassifiers = algorithm.getTotalClassifier();
		Metric metric = new ErrorRate();
		RuleBasedClassifier winner = null;
		double minValue = Double.MAX_VALUE;
		for(int i = 0; i < totalClassifiers.size(); i++) {
			double errorRate = (double)metric.metric(totalClassifiers.get(i), train);
			if(errorRate < minValue) {
				minValue = errorRate;
				winner = totalClassifiers.get(i);
			}
		}
		PostProcessing postProcessing = new RemoveNotWinnerProcessing(train);
		postProcessing.postProcess(winner);

		// Test data
		String testDataName = "dataset" + sep + "pima" + sep + "a0_0_pima-10tst.dat";
		DataSet test = new DataSet();
		Input.inputSingleLabelDataSet(test, testDataName);
		double errorRate = (double)metric.metric(winner, test);

		System.out.println();
		System.out.println("Error Rate(Train): " + minValue);
		System.out.println("Error Rate(Test) : " + errorRate);
		System.out.println("[Classifier]");
		System.out.println(winner.toString());

		System.exit(0);
	}

	// 3.2. 数値実験準備
	public static void deterministicTest() {
		String sep = File.separator;

		// Load "Pima" dataset
		String dataName = "dataset" + sep + "pima" + sep + "a0_0_pima-10tra.dat";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		// Initialization Knowledge base
		Knowledge knowledge = HomoTriangleKnowledgeFactory.builder()
								.dimension(train.getNdim())
								.params(HomoTriangle_2_3.getParams())
								.build()
								.create();

		// Test Rule: 0 4 0 3 3 0 0 0
		int[] antecedentIndex = new int[] {0, 4, 0, 3, 3, 0, 0, 0};
		Antecedent antecedent = Antecedent.builder()
								.knowledge(knowledge)
								.antecedentIndex(antecedentIndex)
								.build();
		ConsequentFactory consequentFactory = new MoFGBML_Learning(train);
		Consequent consequent = consequentFactory.learning(antecedent);
		Rule rule = Rule.builder()
						.antecedent(antecedent)
						.consequent(consequent)
						.build();
		// Check: class=0, weight=0.504
		System.out.println("[Rule]");
		System.out.println(rule.toString());
		System.out.println();

		/* Test Fitnesses
		 * Rule: 0 0 0 2 0 0 0 0, Fitness: 179
		 * Rule: 0 4 0 0 3 0 0 4, Fitness: 136
		 * Rule: 0 1 0 3 1 2 0 0, Fitness: 134
		 */
		Antecedent[] antecedents = new Antecedent[3];
		antecedents[0] = Antecedent.builder()
				.knowledge(knowledge)
				.antecedentIndex(new int[] {0, 0, 0, 2, 0, 0, 0, 0})
				.build();
		antecedents[1] = Antecedent.builder()
				.knowledge(knowledge)
				.antecedentIndex(new int[] {0, 4, 0, 0, 3, 0, 0, 4})
				.build();
		antecedents[2] = Antecedent.builder()
				.knowledge(knowledge)
				.antecedentIndex(new int[] {0, 1, 0, 3, 1, 2, 0, 0})
				.build();

		int seed = 0;
		ProblemMichiganFGBML<IntegerSolution> problem = new ProblemMichiganFGBML<>(seed, train);
		ArrayList<MichiganSolution> population = new ArrayList<>();
		for(int i = 0; i < 3; i++) {
			antecedent = antecedents[i];
			consequent = consequentFactory.learning(antecedent);
			MichiganSolution solution = new MichiganSolution(problem.getBounds(),
													 problem.getNumberOfObjectives(),
													 problem.getNumberOfConstraints(),
													 antecedent,
													 consequent);
			population.add(solution);
		}
		// Evaluate
		System.out.print("[population] ");
		for(MichiganSolution solution : population) {
			System.out.print(solution);
		}
		System.out.println();
	}
}



















