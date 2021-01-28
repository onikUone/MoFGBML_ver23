package cilabo.fuzzy.classifier;

import java.util.ArrayList;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.operator.classification.Classification;
import cilabo.fuzzy.rule.Rule;

public class RuleBasedClassifier implements Classifier {
	// ************************************************************
	// Fields

	/**  */
	ArrayList<Rule> ruleSet = new ArrayList<>();

	/**  */
	Classification classification;

	// ************************************************************
	// Constructor
	public RuleBasedClassifier() {}

	// ************************************************************
	// Methods

	/**
	 *
	 */
	@Override
	public Rule classify(InputVector vector) {
		return (Rule)this.classification.classify(this, vector);
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
	public void addRule(Rule rule) {
		this.ruleSet.add(rule);
	}

	public Rule getRule(int index) {
		return this.ruleSet.get(index);
	}

	public ArrayList<Rule> getRuleSet() {
		return this.ruleSet;
	}

	/**
	 *
	 */
	public Rule popRule(int index) {
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
