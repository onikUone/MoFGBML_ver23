package cilabo.fuzzy.classifier.operator.classification;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.rule.Rule;

public interface Classification {

	public Rule classify(Classifier classifier, InputVector vector);
}
