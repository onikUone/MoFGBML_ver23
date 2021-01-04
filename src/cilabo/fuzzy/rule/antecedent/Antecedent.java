package cilabo.fuzzy.rule.antecedent;

import cilabo.fuzzy.knowledge.Knowledge;
import jfml.term.FuzzyTermType;

public class Antecedent {
	// ************************************************************
	// Fields
	int[] antecedentIndex;

	FuzzyTermType[] antecedentFuzzySets;

	// ************************************************************
	// Constructor

	/**
	 *
	 * @param knowledge
	 * @param antecedentIndex : Shallow copy
	 */
	public Antecedent(Knowledge knowledge, int[] antecedentIndex) {
		this.antecedentIndex = antecedentIndex;
		this.antecedentFuzzySets = new FuzzyTermType[antecedentIndex.length];
		for(int i = 0; i < antecedentIndex.length; i++) {
			if(antecedentIndex[i] < 0) {
				antecedentFuzzySets[i] = null;
			}
			else {
				antecedentFuzzySets[i] = knowledge.getFuzzySet(i, antecedentIndex[i]);
			}
		}
	}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	public double getCompatibleGrade(double[] x) {
		double grade = 1;
		for(int i = 0; i < x.length; i++) {
			if(antecedentIndex[i] < 0) {
				// categorical
				if(antecedentIndex[i] == (int)x[i]) grade *= 1.0;
				else grade *= 0.0;
			}
			else {
				// numerical
				grade *= antecedentFuzzySets[i].getMembershipValue((float)x[i]);
			}
		}
		return grade;
	}

	/**
	 *
	 */
	public int getDimension() {
		return this.antecedentIndex.length;
	}

	/**
	 *
	 */
	public int getAntecedentIndexAt(int index) {
		return this.antecedentIndex[index];
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public FuzzyTermType getAntecedentFuzzySetAt(int index) {
		return this.antecedentFuzzySets[index];
	}

	/**
	 *
	 */
	public int getRuleLength() {
		int length = 0;
		for(int i = 0; i < antecedentIndex.length; i++) {
			if(antecedentIndex[i] != 0) {
				length++;
			}
		}
		return length;
	}

	@Override
	public String toString() {
		if(antecedentFuzzySets == null) return null;

		String str = antecedentFuzzySets[0].getName();
		for(int i = 1; i < antecedentFuzzySets.length; i++) {
			str += ", " + antecedentFuzzySets[i].getName();
		}
		return str;
	}

	public static AntecedentBuilder builder() {
		return new AntecedentBuilder();
	}

	public static class AntecedentBuilder {
		private Knowledge knowledge;
		private int[] antecedentIndex;

		AntecedentBuilder() {}

		public Antecedent.AntecedentBuilder knowledge(Knowledge knowledge) {
			this.knowledge = knowledge;
			return this;
		}

		public Antecedent.AntecedentBuilder antecedentIndex(int[] antecedentIndex) {
			this.antecedentIndex = antecedentIndex;
			return this;
		}

		/**
		 * @param knowledge : Knowledge
		 * @param antecedentIndex : int[]
		 */
		public Antecedent build() {
			try {
				if(this.knowledge == null) throw new NullPointerException("[knowledge] is not initialized.");
				if(this.antecedentIndex == null) throw new NullPointerException("[antecedentIndex] is not initialized.");

				return new Antecedent(knowledge, antecedentIndex);
			}
			catch(NullPointerException e) {
				System.out.println(e);
				return null;
			}
		}
	}

}
