package fuzzy.rule.factory;

import fuzzy.rule.Antecedent;
import fuzzy.rule.Consequent;

public interface ConsequentFactory {

	public Consequent learning(Antecedent antecedent);
}
