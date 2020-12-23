package main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConstsTest {

	@Test
	public void testSetConsts() {
		int UNIX = Consts.UNIX;

		String source = "testConsts";
		Consts.setConsts(source);

		// Not change (not writen in .properties file)
		int nowUNIX = Consts.UNIX;
		assertEquals(nowUNIX, UNIX);

		// Integer
		assertEquals(Consts.ANTECEDENT_LEN, -1);

		// Double
		double delta = 0.000000001;
		assertEquals(Consts.DONT_CARE_RT, -0.4, delta);

		// String
		assertEquals(Consts.ROOTFOLDER, "testConstsForJUnit");
	}


}
