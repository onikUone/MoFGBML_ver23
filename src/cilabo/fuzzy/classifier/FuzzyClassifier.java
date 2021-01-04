package cilabo.fuzzy.classifier;

import java.util.ArrayList;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.operator.classification.Classification;
import cilabo.fuzzy.rule.FuzzyRule;

public class FuzzyClassifier implements Classifier {
	// ************************************************************
	// Fields

	/**  */
	ArrayList<FuzzyRule> ruleSet = new ArrayList<>();

	/**  */
	Classification classification;

	// ************************************************************
	// Constructor
	public FuzzyClassifier() {}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	@Override
	public FuzzyRule classify(InputVector vector) {
		return (FuzzyRule)this.classification.classify(this, vector);
	}

	/**
	 *
	 */
	public int getRuleNum() {
		return this.ruleSet.size();
	}

	/**
	 *
	 */
	public int getRuleLength() {
		int length = 0;
		for(int i = 0; i < ruleSet.size(); i++) {
			length += ruleSet.get(i).getAntecedent().getRuleLength();
		}
		return length;
	}

	/**
	 *
	 */
	public void addFuzzyRule(FuzzyRule rule) {
		this.ruleSet.add(rule);
	}

	public FuzzyRule getFuzzyRule(int index) {
		return this.ruleSet.get(index);
	}

	public ArrayList<FuzzyRule> getRuleSet() {
		return this.ruleSet;
	}

	/**
	 *
	 */
	public FuzzyRule popRule(int index) {
		return this.ruleSet.remove(index);
	}

	/**
	 *
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		String ln = System.lineSeparator();
		String str = "@classification: " + classification.toString() + ln;
		for(int i = 0; i < ruleSet.size(); i++) {
			str += ruleSet.get(i).toString() + ln;
		}
		return str;
	}



}
