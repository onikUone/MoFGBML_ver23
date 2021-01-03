package fuzzy.factory.example;

import static org.junit.Assert.*;

import org.junit.Test;

import fuzzy.Knowledge;
import fuzzy.membershipParams.HomoTriangle_3_4_5;
import jfml.term.FuzzyTermType;

public class HomoTriangleKnowledgeFactoryTest {

	@Test
	public void testCreate() {
		int dimension = 3;
		float[][] params = HomoTriangle_3_4_5.getParams();

		//actual
		FuzzyTermType[][] fuzzySets = new FuzzyTermType[dimension][params.length+1];
		for(int i = 0; i < dimension; i++) {
			//Don't care
			fuzzySets[i][0] = new FuzzyTermType("0", FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f});
			for(int j = 0; j < params.length; j++) {
				fuzzySets[i][j+1] = new FuzzyTermType(String.valueOf(j+1), FuzzyTermType.TYPE_triangularShape, params[j]);
			}
		}
		Knowledge actual = new Knowledge();
		actual.setFuzzySets(fuzzySets);

		Knowledge expected = HomoTriangleKnowledgeFactory.builder()
							.dimension(dimension)
							.params(params)
							.build()
							.create();

		assertEquals(expected.toString(), actual.toString());
	}
}
