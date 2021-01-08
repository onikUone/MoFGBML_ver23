package cilabo.metric.multilabel;

import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.metric.Metric;
import cilabo.utility.GeneralFunctions;

public class HammingLoss implements Metric {
	// ************************************************************
	// Fields
	/**  */
	DataSet dataset;

	// ************************************************************
	// Constructor
	public HammingLoss(DataSet dataset) {
		this.dataset = dataset;
	}

	// ************************************************************
	// Methods

	/**
	 * @param classifier : FuzzyClassifier
	 * @param Double
	 */
	@Override
	public Double metric(Object... objects) {
		if(objects[0].getClass() == FuzzyClassifier.class) {
			FuzzyClassifier classifier = (FuzzyClassifier)objects[0];
			return metric(classifier);
		}
		else {
			(new IllegalArgumentException()).printStackTrace();
			return null;
		}
	}

	public Double metric(FuzzyClassifier classifier) {
		double size = dataset.getDataSize();	// Number of instances;
		double noClass = dataset.getCnum();		// Number of classes;

		double HammingLoss = 0;
		for(int p = 0; p < size; p++) {
			InputVector vector = dataset.getPattern(p).getInputVector();
			Integer[] trueClass = dataset.getPattern(p).getTrueClass().getClassVector();

			Integer[] classifiedClass = classifier.classify(vector)
													.getConsequent().getClassLabel()
													.getClassVector();

			double distance = GeneralFunctions.HammingDistance(trueClass, classifiedClass);
			HammingLoss += distance / noClass;
		}

		return 100.0 * HammingLoss/size;
	}

	public void setDataSet(DataSet dataset) {
		this.dataset = dataset;
	}

}
