package cilabo.gbml.michigan;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.solution.integersolution.impl.DefaultIntegerSolution;

import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;

public class Michigan extends DefaultIntegerSolution implements IntegerSolution {
	// ************************************************************
	// Fields
	protected Antecedent antecedent = null;
	protected Consequent consequent = null;

	// ************************************************************
	// Constructor
	public Michigan(List<Pair<Integer, Integer>> bounds, int numberOfObjectives) {
		super(bounds, numberOfObjectives, 0);
	}

	public Michigan(Michigan solution) {
		super(solution);
		this.antecedent = solution.getAntecedent().deepcopy();
		this.consequent = solution.getConsequent().deepcopy();
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	public void variable2antecedent(Knowledge knowledge) {
		if(antecedent != null) return;

		int[] antecedentIndex = new int[this.getNumberOfVariables()];
		for(int i = 0; i < antecedentIndex.length; i++) {
			antecedentIndex[i] = this.getVariable(i);
		}
		antecedent = Antecedent.builder()
								.knowledge(knowledge)
								.antecedentIndex(antecedentIndex)
								.build();
	}

	/**
	 *
	 */
	public void antecedent2variables() {
		for(int i = 0; i < antecedent.getDimension(); i++) {
			this.setVariable(i, antecedent.getAntecedentIndexAt(i));
		}
	}

	/**
	 *
	 * @param knowledge
	 * @param consequentFactory
	 */
	public void learningConsequent(Knowledge knowledge, ConsequentFactory consequentFactory) {
		if(consequent != null) return;
		variable2antecedent(knowledge);
		consequent = consequentFactory.learning(antecedent);
	}

	public void setAntecedent(Antecedent antecedent) {
		this.antecedent = antecedent;
	}

	/**
	 *
	 */
	public Antecedent getAntecedent() {
		return this.antecedent;
	}

	public void setConsequent(Consequent consequent) {
		this.consequent = consequent;
	}

	/**
	 *
	 */
	public Consequent getConsequent() {
		return this.consequent;
	}

	/**
	 *
	 */
	@Override
	public Michigan copy() {
		return new Michigan(this);
	}

}
