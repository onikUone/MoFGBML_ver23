package fuzzy.factory.example;

import data.ClassLabel;
import fuzzy.Consequent;
import fuzzy.FuzzyRule;
import fuzzy.RuleWeight;
import fuzzy.factory.FuzzyRuleFactory;

public class MoFGBML_FuzzyRuleFactory extends FuzzyRuleFactory {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor
	public MoFGBML_FuzzyRuleFactory() {}

	// ************************************************************
	// Methods



	/**
	 *
	 */
	@Override
	public FuzzyRule create() {

		double[] confidence = this.calcConfidence();

		ClassLabel classLabel = this.calcClassLabel(confidence);
		RuleWeight ruleWeight = this.calcRuleWeight(classLabel, confidence);

		Consequent consequent = Consequent.builder()
								.consequentClass(classLabel)
								.ruleWeight(ruleWeight)
								.build();

		FuzzyRule fuzzyRule = FuzzyRule.builder()
								.antecedent(antecedent)
								.consequent(consequent)
								.build();
		return fuzzyRule;
	}

}
