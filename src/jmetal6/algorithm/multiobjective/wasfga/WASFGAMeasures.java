package jmetal6.algorithm.multiobjective.wasfga;

import java.util.List;

import jmetal6.operator.crossover.CrossoverOperator;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.problem.Problem;
import jmetal6.solution.Solution;
import jmetal6.util.evaluator.SolutionListEvaluator;
import jmetal6.util.measure.Measurable;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;
import jmetal6.util.measure.impl.DurationMeasure;
import jmetal6.util.measure.impl.SimpleMeasureManager;

/**
 * Implementation of the preference based algorithm named WASF-GA on jMetal5.0
 *
 * @author Jorge Rodriguez
 */
@SuppressWarnings("serial")
public class WASFGAMeasures<S extends Solution<?>> extends WASFGA<S> implements Measurable {
	
	protected CountingMeasure iterations;
	protected DurationMeasure durationMeasure;
	protected SimpleMeasureManager measureManager;
	protected BasicMeasure<List<S>> solutionListMeasure;
	
	/**
	 * Constructor
	 *
	 * @param problem Problem to solve
	 */
	public WASFGAMeasures(Problem<S> problem,
												int populationSize,
												int maxIterations,
												CrossoverOperator<S> crossoverOperator,
												MutationOperator<S> mutationOperator,
												SelectionOperator<List<S>, S> selectionOperator,
												SolutionListEvaluator<S> evaluator,
                        double epsilon,
												List<Double> referencePoint,
												String weightVectorsFileName) {
		
		super(problem,
						populationSize,
						maxIterations,
						crossoverOperator,
						mutationOperator,
						selectionOperator,
						evaluator,
						epsilon,
						referencePoint,
						weightVectorsFileName);
		this.initMeasures();
	}
	
	/**
	 * Constructor
	 *
	 * @param problem Problem to solve
	 */
	public WASFGAMeasures(Problem<S> problem,
												int populationSize,
												int maxIterations,
												CrossoverOperator<S> crossoverOperator,
												MutationOperator<S> mutationOperator,
												SelectionOperator<List<S>, S> selectionOperator,
												SolutionListEvaluator<S> evaluator,
												double epsilon,
												List<Double> referencePoint) {
		this(problem,
						populationSize,
						maxIterations,
						crossoverOperator,
						mutationOperator,
						selectionOperator,
						evaluator,
            epsilon,
            referencePoint,
						"");
	}
	
	@Override
	protected void initProgress() {
		this.iterations.reset();
	}
	
	@Override
	protected void updateProgress() {
		this.iterations.increment();
		solutionListMeasure.push(getResult());
	}
	
	@Override
	protected boolean isStoppingConditionReached() {
		return this.iterations.get() >= maxIterations;
	}
	
	@Override
	public void run() {
		durationMeasure.reset();
		durationMeasure.start();
		super.run();
		durationMeasure.stop();
	}
	
	/* Measures code */
	private void initMeasures() {
		durationMeasure = new DurationMeasure();
		iterations = new CountingMeasure(0);
		solutionListMeasure = new BasicMeasure<>();
		
		measureManager = new SimpleMeasureManager();
		measureManager.setPullMeasure("currentExecutionTime", durationMeasure);
		measureManager.setPullMeasure("currentEvaluation", iterations);
		
		measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
		measureManager.setPushMeasure("currentEvaluation", iterations);
	}
	
	@Override
	public String getName() {
		return "WASFGA";
	}
	
	@Override
	public String getDescription() {
		return "Weighting Achievement Scalarizing Function Genetic Algorithm. Version using Measures";
	}
	
	@Override
	public MeasureManager getMeasureManager() {
		return this.measureManager;
	}
}
