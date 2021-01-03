package cilabo.fuzzy.classifier.factory;

import cilabo.fuzzy.classifier.Classifier;

public interface PostProcessing {
	public Classifier postProcess(Classifier classifier);
}
