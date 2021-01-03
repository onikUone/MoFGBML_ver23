package cilabo.fuzzy.rule.antecedent.factory;

import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.antecedent.AntecedentFactory;
import cilabo.main.Consts;
import random.MersenneTwisterFast;

public class RandomInitialization implements AntecedentFactory {
	// ************************************************************
	// Fields
	MersenneTwisterFast uniqueRnd;

	/**  */
	Knowledge knowledge;

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods
	public void setSeed(int seed) {
		this.uniqueRnd = new MersenneTwisterFast(seed);
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	@Override
	public Antecedent create() {
		int dimension = knowledge.getDimension();
		double dcRate;
		if(Consts.IS_PROBABILITY_DONT_CARE) {
			// Constant Value
			dcRate = Consts.DONT_CARE_RT;
		}
		else {
			// (dimension - Const) / dimension
			dcRate = (double)(((double)dimension - (double)Consts.ANTECEDENT_LEN)/(double)dimension);
		}

		int[] antecedentIndex = new int[dimension];
		for(int n = 0; n < dimension; n++) {
			if(uniqueRnd.nextDoubleIE() < dcRate) {
				// don't care
				antecedentIndex[n] = 0;
			}
			else {
				// Fuzzy Set
				antecedentIndex[n] = 1 + uniqueRnd.nextInt(knowledge.getFuzzySetNum(n));
			}
		}

		return Antecedent.builder()
						.knowledge(knowledge)
						.antecedentIndex(antecedentIndex)
						.build();
	}


	public static RandomInitialization.RandomInitializationBuilder builder(){
		return new RandomInitializationBuilder();
	}

	public static class RandomInitializationBuilder {
		private int seed = -1;
		private Knowledge knowledge;

		RandomInitializationBuilder() {}

		public RandomInitialization.RandomInitializationBuilder seed(int seed) {
			this.seed = seed;
			return this;
		}

		/**
		 * @param seed : int
		 * @param knowledge : Knowledge
		 */
		public RandomInitialization build() {
			try {
				if(this.knowledge == null) throw new NullPointerException("[knowledge] is not set.");
			}
			catch(NullPointerException e) {
				System.out.println(e);
			}
			RandomInitialization factory = new RandomInitialization();
			factory.setSeed(seed);
			factory.setKnowledge(knowledge);
			return factory;
		}
	}

}
