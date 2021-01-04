package cilabo.fuzzy.classifier;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.operator.classification.Classification;
import cilabo.fuzzy.classifier.operator.postProcessing.PostProcessing;
import cilabo.fuzzy.classifier.operator.preProcessing.PreProcessing;
import cilabo.fuzzy.rule.FuzzyRule;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.antecedent.AntecedentFactory;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;

public class FuzzyClassifierFactory implements ClassifierFactory {
	// ************************************************************
	// Fields
	/**  */
	protected PreProcessing preProcessing;

	/**  */
	protected AntecedentFactory antecedentFactory;

	/**  */
	protected ConsequentFactory consequentFactory;

	/**  */
	protected PostProcessing postProcessing;

	/** */
	protected Classification classification;

	/**  */
	protected DataSet train;

	/**  */
	int ruleNum;


	// ************************************************************
	// Constructor
	public FuzzyClassifierFactory() {}

	// ************************************************************
	// Methods

	public void setPreProcessing(PreProcessing preProcessing) {
		this.preProcessing = preProcessing;
	}

	public void setAntecedentFactory(AntecedentFactory antecedentFactory) {
		this.antecedentFactory = antecedentFactory;
	}

	public void setConsequentFactory(ConsequentFactory consequentFactory) {
		this.consequentFactory = consequentFactory;
	}

	public void setPostProcessing(PostProcessing postProcessing) {
		this.postProcessing = postProcessing;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public void setDataSet(DataSet train) {
		this.train = train;
	}

	public void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
	}

	/**
	 *
	 */
	@Override
	public FuzzyClassifier create() {
		FuzzyClassifier classifier = new FuzzyClassifier();
		classifier.setClassification(classification);

		// Pre Processing
		preProcessing.preProcess(classifier);

		// Create rule
		for(int i = 0; i < ruleNum; i++) {
			Antecedent antecedent = antecedentFactory.create();
			Consequent consequent = consequentFactory.learning(antecedent);

			FuzzyRule rule = FuzzyRule.builder()
								.antecedent(antecedent)
								.consequent(consequent)
								.build();

			classifier.addFuzzyRule(rule);
		}

		// Post Processing
		postProcessing.postProcess(classifier);

		return classifier;
	}

	public static FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder builder() {
		return new FuzzyClassifierFactoryBuilder();
	}

	public static class FuzzyClassifierFactoryBuilder {

		private PreProcessing preProcessing;
		private AntecedentFactory antecedentFactory;
		private ConsequentFactory consequentFactory;
		private PostProcessing postProcessing;
		private Classification classification;
		private DataSet train;
		private int ruleNum = -1;

		FuzzyClassifierFactoryBuilder() {}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder preProcessing(PreProcessing preProcessing) {
			this.preProcessing = preProcessing;
			return this;
		}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder antecedentFactory(AntecedentFactory antecedentFactory) {
			this.antecedentFactory = antecedentFactory;
			return this;
		}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder consequentFactory(ConsequentFactory consequentFactory) {
			this.consequentFactory = consequentFactory;
			return this;
		}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder postProcessing(PostProcessing postProcessing) {
			this.postProcessing = postProcessing;
			return this;
		}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder classification(Classification classification) {
			this.classification = classification;
			return this;
		}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder train(DataSet train) {
			this.train = train;
			return this;
		}

		public FuzzyClassifierFactory.FuzzyClassifierFactoryBuilder ruleNum(int ruleNum) {
			this.ruleNum = ruleNum;
			return this;
		}

		public void checkException() {
			try {
				if(this.preProcessing == null) throw new NullPointerException("[preProcessing] is not initialized.");
				if(this.antecedentFactory == null) throw new NullPointerException("[antecedentFactory] is not initialized.");
				if(this.consequentFactory == null) throw new NullPointerException("[consequentFactory] is not initialized.");
				if(this.postProcessing == null) throw new NullPointerException("[postProcessing] is not initialized.");
				if(this.classification == null) throw new NullPointerException("[classification] is not initialized.");
				if(this.train == null) throw new NullPointerException("[train] is not initialized.");
				if(this.ruleNum == -1) throw new IllegalStateException("[ruleNum] is not initialized.");
			}
			catch(NullPointerException e) {
				System.out.println(e);
				return;
			}
		}

		/**
		 * @param preProcessing : PreProcessing
		 * @param antecedentFactory : AntecedentFactory
		 * @param consequentFactory : ConsequentFactory
		 * @param classification : Classification
		 * @param train : DataSet
		 * @param ruleNum : int
		 */
		public void setFactory(FuzzyClassifierFactory factory) {
			factory.setPreProcessing(preProcessing);
			factory.setAntecedentFactory(antecedentFactory);
			factory.setConsequentFactory(consequentFactory);
			factory.setPostProcessing(postProcessing);
			factory.setClassification(classification);
			factory.setDataSet(train);
			factory.setRuleNum(ruleNum);
		}

		/**
		 * @param preProcessing : PreProcessing
		 * @param antecedentFactory : AntecedentFactory
		 * @param consequentFactory : ConsequentFactory
		 * @param classification : Classification
		 * @param train : DataSet
		 * @param ruleNum : int
		 */
		public FuzzyClassifierFactory build() {
			checkException();
			FuzzyClassifierFactory factory = new FuzzyClassifierFactory();
			setFactory(factory);
			return factory;
		}
	}
}
