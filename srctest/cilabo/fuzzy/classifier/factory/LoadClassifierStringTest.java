package cilabo.fuzzy.classifier.factory;

import static org.junit.Assert.*;

import org.junit.Test;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.FuzzyClassifier;
import cilabo.fuzzy.knowledge.Knowledge;

public class LoadClassifierStringTest {
	@Test
	public void testLoadCreate() {
		DataSet train = FuzzyClassifierFactoryTest.makeTestTrain();
		Knowledge knowledge = FuzzyClassifierFactoryTest.makeTestKnowledge();
		FuzzyClassifier classifier = FuzzyClassifierFactoryTest.makeClassifier(train);

		FuzzyClassifier newClassifier = LoadClassifierString.builder()
											.classifierString(classifier.toString())
											.knowledge(knowledge)
											.build()
											.create();

		assertEquals(newClassifier.toString(), classifier.toString());
	}

}
