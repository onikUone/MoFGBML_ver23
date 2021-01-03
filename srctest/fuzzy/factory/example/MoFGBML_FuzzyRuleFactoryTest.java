package fuzzy.factory.example;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import data.ClassLabel;
import data.DataSet;
import fuzzy.Antecedent;
import fuzzy.FuzzyRule;
import fuzzy.Knowledge;
import fuzzy.RuleWeight;
import fuzzy.factory.FuzzyRuleFactory;
import fuzzy.membershipParams.HomoTriangle_2_3_4_5;
import utility.Input;

public class MoFGBML_FuzzyRuleFactoryTest {
	@Test
	public void testProcedure() {
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

		int[] antecedentIndex = new int[] {0, 3, 1, 4};
		Antecedent antecedent = Antecedent.builder()
									.knowledge(knowledge)
									.antecedentIndex(antecedentIndex)
									.build();

		FuzzyRuleFactory factory = new MoFGBML_FuzzyRuleFactory();
		factory.setAntecedent(antecedent);
		factory.setTrain(train);

		double[] expectedConfidence = factory.calcConfidence();

		double[] actualConfidence = new double[] {0.025193291650655383,
												  0.8310064845335126,
												  0.1438002238158321};

		// Confidence Value
		double diff = 0.0000001;
		for(int i = 0; i < expectedConfidence.length; i++) {
			assertEquals(expectedConfidence[i], actualConfidence[i], diff);
		}

		// Consequent Class
		ClassLabel expectedClassLabel = factory.calcClassLabel(expectedConfidence);
		int expectedClass = expectedClassLabel.getClassLabel();
		int trueClass = 1;
		assertEquals(expectedClass, trueClass);

		// Rule Weight
		RuleWeight expectedRuleWeight = factory.calcRuleWeight(expectedClassLabel, expectedConfidence);
		double expectedWeight = expectedRuleWeight.getRuleWeight();
		double actualRuleWeight = 0.6620129690670251;

		assertEquals(expectedWeight, actualRuleWeight, diff);
	}

	@Test
	public void testCreate() {
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

		int[] antecedentIndex = new int[] {0, 3, 1, 4};
		Antecedent antecedent = Antecedent.builder()
									.knowledge(knowledge)
									.antecedentIndex(antecedentIndex)
									.build();

		FuzzyRuleFactory factory = new MoFGBML_FuzzyRuleFactory();
		factory.setAntecedent(antecedent);
		factory.setTrain(train);

		FuzzyRule fuzzyRule = factory.create();

		int trueClass = 1;
		assertEquals((int)fuzzyRule.getConsequent().getClassLabel().getClassLabel(), trueClass);

		double diff = 0.00000001;
		double actualRuleWeight = 0.6620129690670251;
		assertEquals((double)fuzzyRule.getConsequent().getRuleWeight().getRuleWeight(), actualRuleWeight, diff);

	}

}
