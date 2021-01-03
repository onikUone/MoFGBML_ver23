package cilabo.fuzzy.classifier.factory;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.Rule;

public interface Classification {

	public Rule classify(InputVector vector);
}
