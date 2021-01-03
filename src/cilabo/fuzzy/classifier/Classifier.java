package cilabo.fuzzy.classifier;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.Rule;

public interface Classifier {

	public Rule classify(InputVector vector);
}
