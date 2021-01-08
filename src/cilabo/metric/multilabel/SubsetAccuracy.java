package cilabo.metric.multilabel;

import cilabo.data.ClassLabel;
import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.metric.Metric;

public class SubsetAccuracy implements Metric {
	// ************************************************************
	// Fields
	/**  */
	DataSet dataset;

	// ************************************************************
	// Constructor
	public SubsetAccuracy(DataSet dataset) {
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
		double size = dataset.getDataSize();
		double correct = 0;
		for(int p = 0; p < size; p++) {
			InputVector vector = dataset.getPattern(p).getInputVector();
			ClassLabel trueClass = dataset.getPattern(p).getTrueClass();

			ClassLabel classifiedClass = classifier.classify(vector).getConsequent().getClassLabel();

			if( trueClass.toString().equals( classifiedClass.toString() )) {
				correct += 1;
			}
		}

		return 100.0 * correct/size;
	}

	public void setDataSet(DataSet dataset) {
		this.dataset = dataset;
	}

}
