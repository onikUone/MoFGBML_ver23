package fuzzy.rule.example;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import data.ClassLabel;
import fuzzy.rule.Antecedent;
import fuzzy.rule.Consequent;
import fuzzy.rule.RuleWeight;
import fuzzy.rule.factory.ConsequentFactory;
import utility.Parallel;

public class MultiLabel_ConsequentFactory extends MoFGBML_ConsequentFactory implements ConsequentFactory {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/**
	 *
	 */
	@Override
	public Consequent learning(Antecedent antecedent) {
		double[][] confidence = this.calcConfidenceMulti(antecedent);

		ClassLabel classLabel = this.calcClassLabel(confidence);
		RuleWeight ruleWeight = this.calcRuleWeight(classLabel, confidence);

		Consequent consequent = Consequent.builder()
								.consequentClass(classLabel)
								.ruleWeight(ruleWeight)
								.build();
		return consequent;
	}

	/**
	 *
	 */
	public double[][] calcConfidenceMulti(Antecedent antecedent) {
		int Cnum = train.getCnum();
		double[][] confidence = new double[Cnum][2];

		for(int c = 0; c < Cnum; c++) {
			for(int i = 0; i < 2; i++) {
				final int CLASS = c;
				final int ASSOCIATE = i;
				Optional<Double> partSum = null;
				try {
					partSum = Parallel.forkJoinPool.submit( () ->
						train.getPatterns().parallelStream()
						// 結論部クラスベクトルのCLASS番目の要素がASSOCIATEであるパターンを抽出
						.filter(pattern -> pattern.getTrueClass().getClassVector()[CLASS] == ASSOCIATE)
						// 各パターンの入力ベクトルを抽出
						.map(pattern -> pattern.getInputVector().getVector())
						// 各入力ベクトルとantecedentのcompatible gradeを計算
						.map(x -> antecedent.getCompatibleGrade(x))
						// compatible gradeを総和する
						.reduce((sum, grade) -> sum+grade)
					).get();
				}
				catch (InterruptedException | ExecutionException e) {
					System.out.println(e);
					return null;
				}

				confidence[CLASS][ASSOCIATE] = partSum.orElse(0.0);
			}

			double sumAll = confidence[c][0] + confidence[c][1];
			for(int i = 0; i < 2; i++) {
				if(sumAll != 0) {
					confidence[c][i] /= sumAll;
				}
				else {
					confidence[c][i] = 0.0;
				}
			}
		}

		return confidence;
	}

	/**
	 *
	 */
	public ClassLabel calcClassLabel(double[][] confidence) {
		ClassLabel classLabel = new ClassLabel();
		for(int c = 0; c < confidence.length; c++) {
			if(confidence[c][0] > confidence[c][1]) {
				classLabel.addClassLabel(0);
			}
			else if(confidence[c][0] < confidence[c][1]) {
				classLabel.addClassLabel(1);
			}
			else {
				classLabel.addClassLabel(-1);
			}
		}
		return classLabel;
	}

	/**
	 *
	 */
	public RuleWeight calcRuleWeight(ClassLabel classLabel, double[][] confidence) {
		RuleWeight ruleWeight = new RuleWeight();
		for(int c = 0; c < confidence.length; c++) {
			if(classLabel.getClassVector()[c] == -1) {
				ruleWeight.addRuleWeight(0.0);
			}
			else {
				ruleWeight.addRuleWeight(Math.abs(confidence[c][0] - confidence[c][1]));
			}
		}
		return ruleWeight;
	}
}
