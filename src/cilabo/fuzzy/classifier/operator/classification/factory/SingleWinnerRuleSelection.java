package cilabo.fuzzy.classifier.operator.classification.factory;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.fuzzy.classifier.operator.classification.Classification;
import cilabo.fuzzy.rule.FuzzyRule;

public class SingleWinnerRuleSelection implements Classification {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	@Override
	public FuzzyRule classify(Classifier classifier, InputVector vector) {
		if(classifier.getClass() != FuzzyClassifier.class) return null;

		List<FuzzyRule> ruleSet = ((FuzzyClassifier)classifier).getRuleSet();

		boolean canClassify = true;
		double max = -Double.MAX_VALUE;
		int winner = 0;
		for(int q = 0; q < ruleSet.size(); q++) {
			FuzzyRule rule = ruleSet.get(q);
			double membership = rule.getAntecedent().getCompatibleGrade(vector.getVector());
			double CF = rule.getConsequent().getRuleWeight().getRuleWeight();

			double value = membership * CF;
			if(value > max) {
				max = value;
				winner = q;
				canClassify = true;
			}
			else if(value == max) {
				FuzzyRule winnerRule = ruleSet.get(winner);
				// "membership*CF"が同値 かつ 結論部クラスが異なる
				if(rule.getConsequent().getClassLabel().toString() != winnerRule.getConsequent().getClassLabel().toString()) {
					canClassify = false;
				}
			}
		}

		if(canClassify && max > 0) {
			return ruleSet.get(winner);
		}
		else {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.getClass().toString();
	}

}
