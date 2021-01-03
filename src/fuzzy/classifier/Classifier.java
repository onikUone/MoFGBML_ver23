package fuzzy.classifier;

import data.InputVector;
import fuzzy.rule.Rule;

public interface Classifier {

	public Rule classify(InputVector vector);
}
