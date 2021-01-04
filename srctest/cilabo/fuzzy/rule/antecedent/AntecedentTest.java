package cilabo.fuzzy.rule.antecedent;

import static org.junit.Assert.*;

import org.junit.Test;

import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.example.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_3_4_5;
import cilabo.fuzzy.rule.antecedent.Antecedent;

public class AntecedentTest {
	@Test
	public void testAntecedent() {
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

		String expected = antecedent.toString();

		String actual = "0, 2, 1";

		assertEquals(expected, actual);
	}

}
