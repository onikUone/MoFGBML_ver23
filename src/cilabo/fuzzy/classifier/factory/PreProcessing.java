package cilabo.fuzzy.classifier.factory;

import cilabo.fuzzy.classifier.Classifier;

public interface PreProcessing {
	public Classifier preProcess(Classifier classifier);
}
