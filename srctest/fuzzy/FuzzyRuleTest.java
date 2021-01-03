package fuzzy;

import static org.junit.Assert.*;

import org.junit.Test;

import data.ClassLabel;
import fuzzy.factory.example.HomoTriangleKnowledgeFactory;
import fuzzy.membershipParams.HomoTriangle_3_4_5;

public class FuzzyRuleTest {
	@Test
	public void testFuzzyRule() {
		int[] antecedentIndex = new int[] {0, 2, 1};
		int dimension = 3;
		float[][] params = HomoTriangle_3_4_5.getParams();

		Knowledge knowledge = HomoTriangleKnowledgeFactory.builder()
								.dimension(dimension)
								.params(params)
								.build()
								.create();

		Antecedent antecedent = Antecedent.builder()
								.knowledge(knowledge)
								.antecedentIndex(antecedentIndex)
								.build();

		ClassLabel classLabel = new ClassLabel();
		classLabel.addClassLabel(7);

		RuleWeight ruleWeight = new RuleWeight();
		ruleWeight.addRuleWeight(0.5);

		Consequent consequent = Consequent.builder()
								.consequentClass(classLabel)
								.ruleWeight(ruleWeight)
								.build();

		FuzzyRule rule = FuzzyRule.builder()
						.antecedent(antecedent)
						.consequent(consequent)
						.build();

		String actual = "If [0, 2, 1] Then class:[7],weight:[0.5]";

		String expected = rule.toString();

		assertEquals(expected, actual);
	}
}
