package fuzzy.classifier.factory;

import data.InputVector;
import fuzzy.rule.Rule;

public interface Classification {

	public Rule classify(InputVector vector);
}
