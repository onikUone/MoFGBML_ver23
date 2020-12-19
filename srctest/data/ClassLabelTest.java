package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClassLabelTest {

	@Test
	public void testGetClassLabel() {
		Integer actual = 7;

		ClassLabel classLabel = new ClassLabel(actual);

		Integer expected = classLabel.getClassLabel();

		assertEquals(expected, actual);
	}
}
