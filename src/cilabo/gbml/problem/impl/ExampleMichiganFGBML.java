package cilabo.gbml.problem.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.integersolution.IntegerSolution;

import cilabo.data.DataSet;
import cilabo.data.Pattern;
import cilabo.fuzzy.classifier.RuleBasedClassifier;
import cilabo.fuzzy.classifier.operator.classification.Classification;
import cilabo.fuzzy.classifier.operator.classification.factory.SingleWinnerRuleSelection;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.antecedent.AntecedentFactory;
import cilabo.fuzzy.rule.antecedent.factory.RandomInitialization;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.MoFGBML_Learning;
import cilabo.gbml.ga.solution.MichiganSolution;
import cilabo.gbml.problem.AbstractMichiganGBML_Problem;

public class ExampleMichiganFGBML extends AbstractMichiganGBML_Problem {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor
	public ExampleMichiganFGBML(int seed, DataSet train) {
		setNumberOfVariables(train.getNdim());
		setNumberOfObjectives(1);
		setNumberOfConstraints(0);
		setName("MichiganFGBML_example");

		// Initialization
		Knowledge knowledge = HomoTriangleKnowledgeFactory.builder()
				.dimension(train.getNdim())
				.params(HomoTriangle_2_3.getParams())
				.build()
				.create();
		AntecedentFactory antecedentFactory = RandomInitialization.builder()
				.seed(seed)
				.knowledge(knowledge)
				.train(train)
				.build();
		ConsequentFactory consequentFactory = MoFGBML_Learning.builder()
				.train(train)
				.build();
		setAntecedentFactory(antecedentFactory);
		setConsequentFactory(consequentFactory);

		// Boundary
	    List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
	    List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());
	    for (int i = 0; i < getNumberOfVariables(); i++) {
	      lowerLimit.add(0);
	      upperLimit.add(knowledge.getFuzzySetNum(i));
	    }
	    setVariableBounds(lowerLimit, upperLimit);
	}

	// ************************************************************
	// Methods

	public double michiganEvaluate(DataSet data, List<MichiganSolution> population) {
		Map<String, MichiganSolution> map = new HashMap<>();

		// Make classifier
		RuleBasedClassifier classifier = new RuleBasedClassifier();

		Classification classification = new SingleWinnerRuleSelection();
		classifier.setClassification(classification);

		for(int i = 0; i < population.size(); i++) {
			Rule rule = population.get(i).getRule();
			classifier.addRule(rule);

			if(!map.containsKey(rule.toString())) {
				map.put(rule.toString(), population.get(i));
			}
		}

		// Evaluation
		double correctRate = 0;
		for(int i = 0; i < data.getDataSize(); i++) {
			Pattern pattern = data.getPattern(i);
			Rule winnerRule = classifier.classify(pattern.getInputVector());
			/* If a winner rule correctly classify a pattern,
			 * then the winner rule's fitness will be incremented. */
			if( winnerRule != null &&
				pattern.getTrueClass().toString()
					.equals(winnerRule.getConsequent().getClassLabel().toString())) {
				MichiganSolution winnerMichigan = map.get(winnerRule.toString());
				this.evaluate(winnerMichigan);
				correctRate += 1;
			}
		}
		correctRate /= (double)data.getDataSize();
		
		return correctRate;
	}

	@Override
	public MichiganSolution createSolution() {
		Antecedent antecedent = antecedentFactory.create();
		Consequent consequent = consequentFactory.learning(antecedent);

		MichiganSolution solution = new MichiganSolution(this.getBounds(),
														 this.getNumberOfObjectives(),
														 this.getNumberOfConstraints(),
														 antecedent,
														 consequent);
		return solution;
	}

	@Override
	public void evaluate(IntegerSolution solution) {
		solution.setObjective(0, solution.getObjective(0)+1);
		return;
	}


}
