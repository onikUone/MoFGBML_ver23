package cilabo.fuzzy.rule;

import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;

public class FuzzyRule implements Rule {
	// ************************************************************
	// Fields

	/** */
	Antecedent antecedent;
	/** */
	Consequent consequent;

	// ************************************************************
	// Constructor

	/**
	 *
	 * @param antecedent : Shallow copy
	 * @param consequent : Shallow copy
	 */
	public FuzzyRule(Antecedent antecedent, Consequent consequent) {
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	// ************************************************************
	// Methods

	public Antecedent getAntecedent() {
		return this.antecedent;
	}

	public Consequent getConsequent() {
		return this.consequent;
	}

	@Override
	public String toString() {
		String str = "";
		str += "If [" + this.antecedent.toString() + "] ";
		str += "Then " + this.consequent.toString();
		return str;
	}

	public static FuzzyRuleBuilder builder() {
		return new FuzzyRuleBuilder();
	}

	public static class FuzzyRuleBuilder {
		private Antecedent antecedent;
		private Consequent consequent;

		FuzzyRuleBuilder() {}

		public FuzzyRule.FuzzyRuleBuilder antecedent(Antecedent antecedent) {
			this.antecedent = antecedent;
			return this;
		}

		public FuzzyRule.FuzzyRuleBuilder consequent(Consequent consequent) {
			this.consequent = consequent;
			return this;
		}

		/**
		 * @param antecedent : Antecedent
		 * @param consequent : Consequent
		 */
		public FuzzyRule build() {
			return new FuzzyRule(antecedent, consequent);
		}
	}
}
