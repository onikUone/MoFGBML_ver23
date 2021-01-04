package cilabo.metric;

import cilabo.fuzzy.classifier.FuzzyClassifier;

public class RuleLengthMetric implements Metric {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/**
	 * @param classifier : FuzzyClassifier
	 * @return Integer
	 */
	@Override
	public Integer metric(Object...objects) {
		if(objects[0].getClass() == FuzzyClassifier.class) {
			FuzzyClassifier classifier = (FuzzyClassifier)objects[0];
			return metric(classifier);
		}
		else {
			return null;
		}
	}

	public Integer metric(FuzzyClassifier classifier) {
		return classifier.getRuleLength();
	}
}
