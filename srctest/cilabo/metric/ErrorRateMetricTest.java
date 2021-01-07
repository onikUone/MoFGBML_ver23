package cilabo.metric;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.fuzzy.classifier.factory.FuzzyClassifierFactoryTest;
import cilabo.utility.Input;

public class ErrorRateMetricTest {
	@Test
	public void testMetric1() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern1.txt";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		FuzzyClassifier classifier = FuzzyClassifierFactoryTest.makeClassifier(train);

		Metric errorRate = new ErrorRateMetric(train);

		double expected = (double)errorRate.metric(classifier);

		double diff = 0.006;
		assertEquals(expected, 100 - 91.67, diff);
	}

	@Test
	public void testMetric2() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern2.txt";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		FuzzyClassifier classifier = FuzzyClassifierFactoryTest.makeClassifier(train);

		Metric errorRate = new ErrorRateMetric(train);

		double expected = (double)errorRate.metric(classifier);

		double diff = 0.006;
		assertEquals(expected, 100 - 90.00, diff);
	}
}
