package cilabo.gbml.problem;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;

import cilabo.fuzzy.rule.antecedent.AntecedentFactory;
import cilabo.fuzzy.rule.consequent.ConsequentFactory;

public abstract class AbstractMichiganGBML_Problem extends AbstractIntegerProblem {
	// ************************************************************
	// Fields
	/**  */
	protected AntecedentFactory antecedentFactory;

	/**  */
	protected ConsequentFactory consequentFactory;

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/* Setters */
	public void setAntecedentFactory(AntecedentFactory antecedentFactory) {
		this.antecedentFactory = antecedentFactory;
	}

	public void setConsequentFactory(ConsequentFactory consequentFactory) {
		this.consequentFactory = consequentFactory;
	}

	@Override
	public String toString() {
		String ln = System.lineSeparator();
		String str = "";
		str += "AntecedentFactory: " + antecedentFactory.getClass().getCanonicalName() + ln;
		str += "ConsequentFactory: " + consequentFactory.getClass().getCanonicalName() + ln;
		return str;
	}

}
