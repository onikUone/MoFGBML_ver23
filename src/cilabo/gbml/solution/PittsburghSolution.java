package cilabo.gbml.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.uma.jmetal.solution.AbstractSolution;
import org.uma.jmetal.solution.Solution;

import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.RuleBasedClassifier;
import cilabo.fuzzy.classifier.operator.classification.Classification;

public class PittsburghSolution extends AbstractSolution<Integer> implements Solution<Integer> {
	// ************************************************************
	// Fields
	/**   */
	List<MichiganSolution> michiganPopulation;

	/**  */
	Classifier classifier;

	// ************************************************************
	// Constructor

	/** Constructor */
	public PittsburghSolution(int numberOfObjectives, List<MichiganSolution> michiganPopulation, Classification classification) {
		this(numberOfObjectives, 0, michiganPopulation, classification);
	}

	/** Constructor */
	public PittsburghSolution(int numberOfObjectives, int numberOfConstraints,
								List<MichiganSolution> michiganPopulation, Classification classification)
	{
		/* ruleNum*Ndim: The number of variables, the length of that is variable. */
		super(michiganPopulation.size()*michiganPopulation.get(0).getNumberOfVariables(),
				numberOfObjectives, numberOfConstraints);

		// Build classifier from michigan population.
		classifier = new RuleBasedClassifier();
		((RuleBasedClassifier)classifier).setClassification(classification);
		for(int i = 0; i < michiganPopulation.size(); i++) {
			MichiganSolution michigan = michiganPopulation.get(i);
			((RuleBasedClassifier)classifier).addRule(michigan.getRule());
			// Set variable from michigan-type solution.
			for(int j = 0; j < michigan.getNumberOfVariables(); j++) {
				setVariable(i*michigan.getNumberOfVariables() + j, michigan.getVariable(j));
			}
		}
	}

	/** Copy constructor */
	public PittsburghSolution(PittsburghSolution solution) {
		super(solution.getNumberOfVariables(), solution.getNumberOfObjectives(), solution.getNumberOfConstraints());
		for(int i = 0; i < solution.getNumberOfVariables(); i++) {
			setVariable(i, solution.getVariable(i));
		}
		for(int i = 0; i < solution.getNumberOfObjectives(); i++) {
			setObjective(i, solution.getObjective(i));
		}
		for(int i = 0; i < solution.getNumberOfConstraints(); i++) {
			setConstraint(i, solution.getConstraint(i));
		}
		attributes = new HashMap<>(solution.attributes);

		michiganPopulation = new ArrayList<>(solution.getMichiganPopulation().size());
		for(int i = 0; i < michiganPopulation.size(); i++) {
			michiganPopulation.add(i, solution.getMichiganPopulation().get(i).copy());
		}
		classifier = solution.getClassifier().copy();
	}

	// ************************************************************
	// Methods

	@Override
	public PittsburghSolution copy() {
		return new PittsburghSolution(this);
	}

	/* Getters */
	public List<MichiganSolution> getMichiganPopulation() {
		return this.michiganPopulation;
	}

	public Classifier getClassifier() {
		return this.classifier;
	}

}
