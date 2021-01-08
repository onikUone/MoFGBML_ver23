package cilabo.metric.multilabel;

import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.metric.Metric;

public class Fmeasure implements Metric {
	// ************************************************************
	// Fields
	/**  */
	DataSet dataset;

	// ************************************************************
	// Constructor
	public Fmeasure(DataSet dataset) {
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

		double recall = 0.0;
		double precision = 0.0;
		for(int p = 0; p < size; p++) {
			InputVector vector = dataset.getPattern(p).getInputVector();
			Integer[] trueClass = dataset.getPattern(p).getTrueClass().getClassVector();

			Integer[] classifiedClass = classifier.classify(vector)
					.getConsequent().getClassLabel()
					.getClassVector();

			precision += Precision.PrecisionMetric(classifiedClass, trueClass);
			recall += Recall.RecallMetric(classifiedClass, trueClass);
		}
		recall = recall/size;
		precision = precision/size;

		double Fmeasure;
		if((precision + recall) == 0) Fmeasure = 0;
		else {
			Fmeasure = (2.0 * recall * precision) / (recall + precision);
		}
		return 100.0 * Fmeasure;
	}

	public void setDataSet(DataSet dataset) {
		this.dataset = dataset;
	}

}
