package fuzzy;

import data.ClassLabel;

public class Consequent {
	// ************************************************************
	// Fields

	/**  */
	ClassLabel consequentClass;

	/**  */
	RuleWeight ruleWeight;

	// ************************************************************
	// Constructor

	/**
	 * Shallow Copy
	 * @param C
	 * @param weight
	 */
	public Consequent(ClassLabel C, RuleWeight weight) {
		this.consequentClass = C;
		this.ruleWeight = weight;
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	public ClassLabel getClassLabel() {
		return this.consequentClass;
	}

	/**
	 *
	 */
	public RuleWeight getRuleWeight() {
		return this.ruleWeight;
	}

	@Override
	public String toString() {
		String str = "";

		// ClassLabel
		str += "class:[";
		str += this.consequentClass.toString();
		str += "]";

		str += " ";

		// RuleWeight
		str += "weight:[";
		str += this.ruleWeight.toString();
		str += "]";

		return str;
	}
}
