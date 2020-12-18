package jmetal6.algorithm.multiobjective.dmopso;

import java.util.List;

import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.qualityindicator.impl.Epsilon;
import jmetal6.qualityindicator.impl.hypervolume.PISAHypervolume;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.front.Front;
import jmetal6.util.front.imp.ArrayFront;
import jmetal6.util.measure.Measurable;
import jmetal6.util.measure.MeasureManager;
import jmetal6.util.measure.impl.BasicMeasure;
import jmetal6.util.measure.impl.CountingMeasure;
import jmetal6.util.measure.impl.DurationMeasure;
import jmetal6.util.measure.impl.SimpleMeasureManager;

@SuppressWarnings("serial")
public class DMOPSOMeasures extends DMOPSO implements Measurable {

  protected CountingMeasure iterations;
  protected DurationMeasure durationMeasure;
  protected SimpleMeasureManager measureManager;
  protected BasicMeasure<List<DoubleSolution>> solutionListMeasure;
  protected BasicMeasure<Double> hypervolumeValue;
  protected BasicMeasure<Double> epsilonValue;
  protected Front referenceFront;

  public DMOPSOMeasures(DoubleProblem problem, int swarmSize, int maxIterations,
                        double r1Min, double r1Max, double r2Min, double r2Max,
                        double c1Min, double c1Max, double c2Min, double c2Max,
                        double weightMin, double weightMax, double changeVelocity1, double changeVelocity2,
                        FunctionType functionType, String dataDirectory, int maxAge) {
    this(problem, swarmSize, maxIterations,
            r1Min, r1Max, r2Min, r2Max,
            c1Min, c1Max, c2Min, c2Max,
            weightMin, weightMax, changeVelocity1, changeVelocity2,
            functionType, dataDirectory, maxAge, "dMOPSO");
  }

  public DMOPSOMeasures(DoubleProblem problem, int swarmSize, int maxIterations,
                        double r1Min, double r1Max, double r2Min, double r2Max,
                        double c1Min, double c1Max, double c2Min, double c2Max,
                        double weightMin, double weightMax, double changeVelocity1, double changeVelocity2,
                        FunctionType functionType, String dataDirectory, int maxAge, String name) {
    super(problem, swarmSize, maxIterations,
            r1Min, r1Max, r2Min, r2Max,
            c1Min, c1Max, c2Min, c2Max,
            weightMin, weightMax, changeVelocity1, changeVelocity2,
            functionType, dataDirectory, maxAge, name);
    this.referenceFront = new ArrayFront();
    initMeasures();
  }

  @Override
  protected void initProgress() {
    this.iterations.reset();
  }

  @Override
  protected void updateProgress() {
    this.iterations.increment();
    hypervolumeValue.push(new PISAHypervolume<DoubleSolution>(referenceFront).evaluate(getResult()));
    epsilonValue.push(new Epsilon<DoubleSolution>(referenceFront).evaluate(getResult()));
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
    hypervolumeValue = new BasicMeasure<>();
    epsilonValue = new BasicMeasure<>();

    measureManager = new SimpleMeasureManager();
    measureManager.setPullMeasure("currentExecutionTime", durationMeasure);
    measureManager.setPullMeasure("currentEvaluation", iterations);
    measureManager.setPullMeasure("hypervolume", hypervolumeValue);
    measureManager.setPullMeasure("epsilon", epsilonValue);

    measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
    measureManager.setPushMeasure("currentEvaluation", iterations);
    measureManager.setPushMeasure("hypervolume", hypervolumeValue);
    measureManager.setPushMeasure("epsilon", epsilonValue);
  }

  @Override
  public String getDescription() {
    return "MOPSO with decomposition. Version using measures";
  }

  @Override
  public MeasureManager getMeasureManager() {
    return this.measureManager;
  }

  public void setReferenceFront(Front referenceFront) {
    this.referenceFront = referenceFront;
  }
}