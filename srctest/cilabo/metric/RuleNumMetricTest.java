package cilabo.metric;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.fuzzy.classifier.factory.FuzzyClassifierFactoryTest;
import cilabo.utility.Input;

public class RuleNumMetricTest {
	@Test
	public void testMetric() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern1.txt";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		FuzzyClassifier classifier = FuzzyClassifierFactoryTest.makeClassifier(train);

		Metric ruleNum = new RuleNumMetric();

		int expected = (int)ruleNum.metric(classifier);

		assertEquals(expected, 13);
	}
}
