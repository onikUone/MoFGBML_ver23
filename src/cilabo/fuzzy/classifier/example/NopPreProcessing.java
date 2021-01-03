package cilabo.fuzzy.classifier.example;

import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.factory.PreProcessing;

public class NopPreProcessing implements PreProcessing {

	@Override
	public Classifier preProcess(Classifier classifier) {
		// No operate
		return classifier;
	}

}
