package cilabo.fuzzy.rule.antecedent.factory;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import cilabo.data.DataSet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.example.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.membershipParams.HomoTriangle_2_3_4_5;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.utility.Input;

public class HeuristicRuleGenerationMethodTest {
	@Test
	public void testHeuristicRuleGeneration() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "iris" + sep + "a0_0_iris-10tra.dat";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		int dimension = train.getNdim();
		float[][] params = HomoTriangle_2_3_4_5.getParams();
		Knowledge knowledge = HomoTriangleKnowledgeFactory.builder()
								.dimension(dimension)
								.params(params)
								.build()
								.create();


		Integer[] samplingIndex = new Integer[] {0, 1};
		HeuristicRuleGenerationMethod factory = new HeuristicRuleGenerationMethod();
		factory.setKnowledge(knowledge);
		factory.setSeed(0);
		factory.setSamplingIndex(samplingIndex);
		factory.setTrain(train);

		Antecedent antecedent = factory.create();

		String actual = "7, 8, 6, 10";
		String expected = antecedent.toString();

		assertEquals(expected, actual);

		antecedent = factory.create();
		actual = "3, 7, 10, 1";
		expected = antecedent.toString();
		assertEquals(expected, actual);

		antecedent = factory.create();
		assertEquals(null, antecedent);

	}
}