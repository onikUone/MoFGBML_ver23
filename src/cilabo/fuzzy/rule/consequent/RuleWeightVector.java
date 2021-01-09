package cilabo.fuzzy.rule.consequent;

public class RuleWeightVector extends RuleWeight {
	// ************************************************************
	// Fields
	Double CFmean = null;

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/**
	 * @return CFmean : Average of rule weight vector
	 */
	@Override
	public Double getRuleWeight() {
		if(this.CFmean == null) {
			this.CFmean = 0.0;
			double length = (double)this.ruleWeight.size();
			for(int i = 0; i < length; i++) {
				this.CFmean += this.ruleWeight.get(i);
			}
			this.CFmean /= length;
		}

		return this.CFmean;
	}

	/**
	 * @param index
	 */
	public Double getRuleWeightForClass(int index) {
		return this.ruleWeight.get(index);
	}

	/**
	 *
	 */
	@Override
	public void addRuleWeight(Double weight) {
		this.CFmean = null;
		this.ruleWeight.add(weight);
	}

	/**
	 *
	 */
	@Override
	public void addRuleWeightVector(Double[] weight) {
		this.CFmean = null;
		for(int i = 0; i < weight.length; i++) {
			this.ruleWeight.add(weight[i]);
		}
	}


}
