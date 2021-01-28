package cilabo.gbml.example;

import java.io.File;
import java.util.ArrayList;

import cilabo.data.DataSet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.MoFGBML_Learning;
import cilabo.gbml.ga.solution.MichiganSolution;
import cilabo.gbml.problem.impl.ExampleMichiganFGBML;
import cilabo.utility.Input;

/**
 * For student belonging in CILAB,
 * this example is implementation of the programming exercise 6.
 */
public class ExampleMichiganFGBML_main {

	public static void main(String[] args) {
		deterministicTest();
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
		ExampleMichiganFGBML problem = new ExampleMichiganFGBML(seed, train);
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
		double correctRate =
				problem.michiganEvaluate(train, population);
		System.out.print("[population] ");
		System.out.println("Error Rate = " + (100-100*correctRate));
		for(MichiganSolution solution : population) {
			System.out.print(solution);
		}
		System.out.println();
	}
}



