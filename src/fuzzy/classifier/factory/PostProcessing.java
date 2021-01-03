package fuzzy.classifier.factory;

import fuzzy.classifier.Classifier;

public interface PostProcessing {
	public Classifier postProcess(Classifier classifier);
}
