package cilabo.fuzzy.classifier;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import cilabo.data.DataSet;
import cilabo.data.Pattern;
import cilabo.fuzzy.classifier.operator.classification.Classification;
import cilabo.fuzzy.classifier.operator.classification.factory.SingleWinnerRuleSelection;
import cilabo.fuzzy.classifier.operator.postProcessing.PostProcessing;
import cilabo.fuzzy.classifier.operator.postProcessing.factory.SimplePostProcessing;
import cilabo.fuzzy.classifier.operator.preProcessing.PreProcessing;
import cilabo.fuzzy.classifier.operator.preProcessing.factory.NopPreProcessing;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_3;
import cilabo.fuzzy.rule.FuzzyRule;
import cilabo.fuzzy.rule.antecedent.AntecedentFactory;
import cilabo.fuzzy.rule.antecedent.factory.AllCombinationAntecedentFactory;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.MoFGBML_Learning;
import cilabo.utility.Input;

public class FuzzyClassifierFactoryTest {
	public static FuzzyClassifier makeClassifier(DataSet train) {
		int dimension = train.getNdim();
		float[][] params = HomoTriangle_3.getParams();
		Knowledge knowledge = HomoTriangleKnowledgeFactory.builder()
								.dimension(dimension)
								.params(params)
								.build()
								.create();

		PreProcessing preProcessing = new NopPreProcessing();

		AntecedentFactory antecedentFactory = AllCombinationAntecedentFactory.builder()
												.knowledge(knowledge)
												.build();
		int ruleNum = ((AllCombinationAntecedentFactory)antecedentFactory).getRuleNum();

		ConsequentFactory consequentFactory = MoFGBML_Learning.builder()
												.train(train)
												.build();

		PostProcessing postProcessing = new SimplePostProcessing();

		Classification classification = new SingleWinnerRuleSelection();

		ClassifierFactory factory = FuzzyClassifierFactory.builder()
										.preProcessing(preProcessing)
										.antecedentFactory(antecedentFactory)
										.consequentFactory(consequentFactory)
										.postProcessing(postProcessing)
										.classification(classification)
										.train(train)
										.ruleNum(ruleNum)
										.build();

		FuzzyClassifier classifier = (FuzzyClassifier)factory.create();
		return classifier;
	}

	public double evaluateCorrectRate(DataSet dataset, FuzzyClassifier classifier) {
		double correct = 0;
		for(int i = 0; i < dataset.getDataSize(); i++) {
			Pattern pattern = dataset.getPattern(i);
			FuzzyRule winnerRule = classifier.classify(pattern.getInputVector());
			if(winnerRule == null) continue;
			if(pattern.getTrueClass().toString().equals(winnerRule.getConsequent().getClassLabel().toString())) {
				correct += 1;
			}
		}
		double rate = 100 * (correct / (double)dataset.getDataSize());
		return rate;
	}

	@Test
	public void testCreateWithKadai5_1() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern1.txt";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		FuzzyClassifier classifier = makeClassifier(train);

		String expected = classifier.toString();
		System.out.println(expected);

		// RuleNum
		assertEquals(classifier.getRuleNum(), 13);

		//RuleLength
		assertEquals(classifier.getRuleLength(), 22);

		// Classification rate
		double diff = 0.006;
		assertEquals(evaluateCorrectRate(train, classifier), 91.67, diff);
	}

	@Test
	public void testCreateWithKadai5_2() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern2.txt";
		DataSet train = new DataSet();
		Input.inputSingleLabelDataSet(train, dataName);

		FuzzyClassifier classifier = makeClassifier(train);

		String expected = classifier.toString();
		System.out.println(expected);

		// RuleNum
		assertEquals(classifier.getRuleNum(), 12);

		//RuleLength
		assertEquals(classifier.getRuleLength(), 20);

		// Classification rate
		double diff = 0.006;
		assertEquals(evaluateCorrectRate(train, classifier), 90.00, diff);
	}
}
