package fuzzy.rule;

import static org.junit.Assert.*;

import org.junit.Test;

import fuzzy.knowledge.Knowledge;
import fuzzy.knowledge.example.HomoTriangleKnowledgeFactory;
import fuzzy.membershipParams.HomoTriangle_3_4_5;
import fuzzy.rule.Antecedent;

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
