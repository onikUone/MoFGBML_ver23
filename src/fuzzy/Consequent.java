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

		str += ",";

		// RuleWeight
		str += "weight:[";
		str += this.ruleWeight.toString();
		str += "]";

		return str;
	}

	/**
	 *
	 */
	public static ConsequentBuilder builder() {
		return new ConsequentBuilder();
	}

	public static class ConsequentBuilder {
		private ClassLabel consequentClass;
		private RuleWeight ruleWeight;

		ConsequentBuilder() {}

		public Consequent.ConsequentBuilder consequentClass(ClassLabel consequentClass) {
			this.consequentClass = consequentClass;
			return this;
		}

		public Consequent.ConsequentBuilder ruleWeight(RuleWeight ruleWeight) {
			this.ruleWeight = ruleWeight;
			return this;
		}

		/**
		 * @param consequentClass : ClassLabel
		 * @param ruleWeight : RuleWeight
		 */
		public Consequent build() {
			try {
				if(this.consequentClass == null) throw new NullPointerException("[consequentClass] is not initialized.");
				if(this.ruleWeight == null) throw new NullPointerException("[ruleWeight] is not initialized.");

				return new Consequent(consequentClass, ruleWeight);
			}
			catch(NullPointerException e) {
				System.out.println(e);
				return null;
			}
		}
	}
}
