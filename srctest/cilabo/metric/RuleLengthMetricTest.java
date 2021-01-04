package cilabo.metric;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.fuzzy.classifier.FuzzyClassifierFactoryTest;
import cilabo.utility.Input;

public class RuleLengthMetricTest {
	@Test
	public void testMetric() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern1.txt";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		FuzzyClassifier classifier = FuzzyClassifierFactoryTest.makeClassifier(train);

		Metric ruleLength = new RuleLengthMetric();

		int expected = (int)ruleLength.metric(classifier);

		assertEquals(expected, 22);
	}
}
