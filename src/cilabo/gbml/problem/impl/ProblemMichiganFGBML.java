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

public class ProblemMichiganFGBML<S extends IntegerSolution> extends AbstractMichiganGBML_Problem<S> {
	// ************************************************************
	// Fields
	private Knowledge knowledge;
	private DataSet evaluationDataset;

	// ************************************************************
	// Constructor
	public ProblemMichiganFGBML(int seed, DataSet train) {
		this.evaluationDataset = train;
		setNumberOfVariables(train.getNdim());
		setNumberOfObjectives(1);
		setNumberOfConstraints(0);
		setName("MichiganFGBML");

		// Initialization
		this.knowledge = HomoTriangleKnowledgeFactory.builder()
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

	/* Getter */
	public Knowledge getKnowledge() {
		return this.knowledge;
	}

	/* Setter */
	public void setEvaluationDataset(DataSet evaluationDataset) {
		this.evaluationDataset = evaluationDataset;
	}

	public List<S> michiganEvaluate(List<S> population) {
		if( population.size() == 0 ||
			population.get(0).getClass() != MichiganSolution.class)
			return null;

		// Clear fitness
		population.stream().forEach(s -> s.setObjective(0, 0.0));

		Map<String, S> map = new HashMap<>();

		// Make classifier
		RuleBasedClassifier classifier = new RuleBasedClassifier();

		Classification classification = new SingleWinnerRuleSelection();
		classifier.setClassification(classification);

		for(int i = 0; i < population.size(); i++) {
			Rule rule = ((MichiganSolution)population.get(i)).getRule();
			classifier.addRule(rule);

			if(!map.containsKey(rule.toString())) {
				map.put(rule.toString(), population.get(i));
			}
		}

		// Evaluation
		for(int i = 0; i < evaluationDataset.getDataSize(); i++) {
			Pattern pattern = evaluationDataset.getPattern(i);
			Rule winnerRule = classifier.classify(pattern.getInputVector());
			/* If a winner rule correctly classify a pattern,
			 * then the winner rule's fitness will be incremented. */
			if( winnerRule != null &&
				pattern.getTrueClass().toString()
					.equals(winnerRule.getConsequent().getClassLabel().toString())) {
				S winnerMichigan = map.get(winnerRule.toString());
				this.evaluate((IntegerSolution)winnerMichigan);
			}
		}

		return population;
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
