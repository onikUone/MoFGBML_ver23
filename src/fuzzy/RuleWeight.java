package fuzzy;

import java.util.ArrayList;

public class RuleWeight {
	// ************************************************************
	// Fields
	ArrayList<Double> ruleWeight = new ArrayList<>();

	// ************************************************************
	// Constructor
	public RuleWeight() {}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	public Double getRuleWeight() {
		return this.ruleWeight.get(0);
	}

	/**
	 *
	 */
	public Double[] getRuleWeightVector() {
		return this.ruleWeight.toArray(new Double[0]);
	}

	/**
	 *
	 */
	public void addRuleWeight(Double weight) {
		this.ruleWeight.add(weight);
	}

	/**
	 *
	 */
	public void addRuleWeightVector(Double[] weight) {
		for(int i = 0; i < weight.length; i++) {
			this.ruleWeight.add(weight[i]);
		}
	}

	@Override
	public String toString() {
		if(this.ruleWeight.size() == 0) {
			return null;
		}

		String str = String.valueOf(this.ruleWeight.get(0));
		for(int i = 1; i < this.ruleWeight.size(); i++) {
			str += ", " + this.ruleWeight.get(i);
		}
		return str;
	}

}
