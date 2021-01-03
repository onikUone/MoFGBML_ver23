package fuzzy.classifier.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fuzzy.classifier.Classifier;
import fuzzy.classifier.FuzzyClassifier;
import fuzzy.classifier.factory.PostProcessing;
import fuzzy.rule.Antecedent;
import fuzzy.rule.FuzzyRule;

public class SimplePostProcessing implements PostProcessing {

	@Override
	public Classifier postProcess(Classifier classifier) {
		try {
			if(classifier.getClass() != FuzzyClassifier.class) throw new IllegalArgumentException("argument must be [FuzzyClassifier]");
		}
		catch(IllegalArgumentException e) {
			System.out.println(e);
			return null;
		}

		classifier = remove((FuzzyClassifier)classifier);
		classifier = removeSameAntecedent((FuzzyClassifier)classifier);
		classifier = radixSort((FuzzyClassifier)classifier);

		return classifier;
	}

	public FuzzyClassifier remove(FuzzyClassifier classifier) {
		int originalRuleNum = classifier.getRuleNum();
		for(int i = originalRuleNum-1; i >= 0; i--) {
			FuzzyRule rule = classifier.getFuzzyRule(i);
			if( rule.getConsequent().getRuleWeight().getRuleWeight() <= 0 ||
				rule.getAntecedent().getRuleLength() == 0) {
				classifier.popRule(i);
			}
		}
		return classifier;
	}

	public FuzzyClassifier removeSameAntecedent(FuzzyClassifier classifier) {
		ArrayList<Integer> sameList = new ArrayList<>();
		// Trace
		for(int i = 0; i < classifier.getRuleNum(); i++) {
			for(int j = 0; j < i; j++) {
				if(!sameList.contains(j)) {
					Antecedent origin = classifier.getFuzzyRule(i).getAntecedent();
					Antecedent object = classifier.getFuzzyRule(j).getAntecedent();
					if(origin.toString() == object.toString()) {
						sameList.add(i);
					}
				}
			}
		}
		// Remove
		for(int i = 0; i < sameList.size(); i++) {
			classifier.popRule(sameList.get(i) - i);
		}
		return classifier;
	}

	public FuzzyClassifier radixSort(FuzzyClassifier classifier) {
		Collections.sort(classifier.getRuleSet(), new Comparator<FuzzyRule>() {
			@Override
			public int compare(FuzzyRule aa, FuzzyRule bb) {
				Antecedent a = aa.getAntecedent();
				Antecedent b = bb.getAntecedent();
				int dimension = a.getDimension();

				for(int i = 0; i < dimension; i++) {
					if(a.getAntecedentIndexAt(i) < b.getAntecedentIndexAt(i)) {
						return -1;
					}
					else if(a.getAntecedentIndexAt(i) > b.getAntecedentIndexAt(i)) {
						return 1;
					}
					else {
						continue;
					}
				}
				return 0;
			}
		});
		return classifier;
	}

}
