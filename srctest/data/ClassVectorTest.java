package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassVectorTest {

	@Test
	public void testGetClassLabel() {
		Integer[] actual = new Integer[] {1, 0, 1};

		ClassVector classVector = new ClassVector(actual);

		Integer[] expected = classVector.getClassLabel();

		assertArrayEquals(expected, actual);
	}
}
