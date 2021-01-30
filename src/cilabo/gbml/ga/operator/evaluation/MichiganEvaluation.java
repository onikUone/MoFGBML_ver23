package cilabo.gbml.ga.operator.evaluation;

import java.util.List;

import org.uma.jmetal.component.evaluation.impl.AbstractEvaluation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import cilabo.gbml.problem.impl.ProblemMichiganFGBML;

public class MichiganEvaluation<S extends IntegerSolution> extends AbstractEvaluation<S> {
	public MichiganEvaluation() {
		super(new MichiganEvaluator<>());
	}
}

class MichiganEvaluator<S extends IntegerSolution> implements SolutionListEvaluator<S> {

	@SuppressWarnings("unchecked")
	@Override
	public List<S> evaluate(List<S> solutionList, Problem<S> problem) throws JMetalException {
		if(problem.getClass() != ProblemMichiganFGBML.class) {
			return null;
		}
		else {
			return ((ProblemMichiganFGBML<S>)problem).michiganEvaluate(solutionList);
		}
	}

	@Override
	public void shutdown() {
		// This method is an intentionally-blank override.
	}
}
