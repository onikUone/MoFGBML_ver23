package cilabo.fuzzy.rule.antecedent.factory;

import java.util.ArrayList;
import java.util.Arrays;

import cilabo.data.DataSet;
import cilabo.data.Pattern;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.antecedent.AntecedentFactory;
import cilabo.main.Consts;

public class HeuristicRuleGenerationMethod extends RandomInitialization implements AntecedentFactory {
	// ************************************************************
	// Fields

	/**  */
	DataSet train;

	/**  */
	ArrayList<Integer> samplingIndex;

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/**
	 *
	 */
	@Override
	public Antecedent create() {
		if(samplingIndex.size() == 0) {
			return null;
		}

		int dimension = train.getNdim();
		Pattern pattern = train.getPattern(samplingIndex.remove(0));
		double[] vector = pattern.getInputVector().getVector();

		double dcRate;
		if(Consts.IS_PROBABILITY_DONT_CARE) {
			dcRate = Consts.DONT_CARE_RT;
		}
		else {
			// (Ndim - const) / Ndim
			dcRate = (double)(((double)dimension - (double)Consts.ANTECEDENT_LEN)/(double)dimension);
		}

		int[] antecedentIndex = new int[dimension];
		for(int n = 0; n < dimension; n++) {
			// don't care
			if(uniqueRnd.nextDouble() < dcRate) {
				antecedentIndex[n] = 0;
				continue;
			}

			// Categorical Judge
			if(vector[n] < 0) {
				antecedentIndex[n] = (int)vector[n];
				continue;
			}

			// Numerical
			int fuzzySetNum = knowledge.getFuzzySetNum(n)-1;
			double[] membershipValueRoulette = new double[fuzzySetNum];
			double sumMembershipValue = 0;
			membershipValueRoulette[0] = 0;
			for(int h = 0; h < fuzzySetNum; h++) {
				sumMembershipValue += knowledge.getMembershipValue(vector[n], n, h+1);
				membershipValueRoulette[h] = sumMembershipValue;
			}

			double arrow = uniqueRnd.nextDouble() * sumMembershipValue;
			for(int h = 0; h < fuzzySetNum; h++) {
				if(arrow < membershipValueRoulette[h]) {
					antecedentIndex[n] = h+1;
					break;
				}
			}

		}

		return Antecedent.builder()
				.knowledge(knowledge)
				.antecedentIndex(antecedentIndex)
				.build();
	}

	/**
	 * Shallow copy
	 */
	public void setTrain(DataSet train) {
		this.train = train;
	}

	/**
	 * Shallow copy
	 */
	public void setSamplingIndex(Integer[] samplingIndex) {
		this.samplingIndex = new ArrayList<Integer>();
		this.samplingIndex.addAll(Arrays.asList(samplingIndex));
	}

	public static HeuristicRuleGenerationMethod.HeuristicRuleGenerationMethodBuilder builder() {
		return new HeuristicRuleGenerationMethodBuilder();
	}

	public static class HeuristicRuleGenerationMethodBuilder extends RandomInitializationBuilder {
		private DataSet train;
		private Integer[] samplingIndex;

		HeuristicRuleGenerationMethodBuilder() {}

		public HeuristicRuleGenerationMethod.HeuristicRuleGenerationMethodBuilder samplingIndex(Integer[] samplingIndex) {
			this.samplingIndex = samplingIndex;
			return this;
		}

		public HeuristicRuleGenerationMethod.HeuristicRuleGenerationMethodBuilder train(DataSet train) {
			this.train = train;
			return this;
		}

		@Override
		public void checkException() {
			super.checkException();
			try {
				if(this.train == null) throw new NullPointerException("[train] is not set.");
			}
			catch (NullPointerException e) {
				System.out.println(e);
			}
		}

		/**
		 * @param seed : int
		 * @param knowledge : Knowledge
		 * @param train : DataSet
		 * @param samplingIndex : Integer[]
		 */
		public void setFactory(HeuristicRuleGenerationMethod factory) {
			super.setFactory(factory);
			factory.setTrain(train);
			factory.setSamplingIndex(samplingIndex);
		}

		/**
		 * @param seed : int
		 * @param knowledge : Knowledge
		 * @param train : DataSet
		 * @param samplingIndex : Integer[]
		 */
		@Override
		public HeuristicRuleGenerationMethod build() {
			checkException();
			HeuristicRuleGenerationMethod factory = new HeuristicRuleGenerationMethod();
			setFactory(factory);
			return factory;
		}
	}
}
