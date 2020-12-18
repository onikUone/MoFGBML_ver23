package jmetal6.lab.experiment.lab;

import java.util.ArrayList;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.AlgorithmBuilder;
import jmetal6.algorithm.multiobjective.nsgaii.jmetal5version.NSGAIIBuilder;
import jmetal6.lab.experiment.util.ExperimentProblem;
import jmetal6.operator.crossover.impl.SBXCrossover;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.Problem;
import jmetal6.problem.multiobjective.zdt.*;
import jmetal6.solution.Solution;
import jmetal6.solution.doublesolution.DoubleSolution;

public class ExperimentTemplate {

  @FunctionalInterface
  public interface AlgorithmWrapper<S extends Solution<?>> {
    Algorithm<List<S>> create(Problem<S> problem);
  }

  public class NSGAIIgg implements AlgorithmWrapper<DoubleSolution> {
    public Algorithm<List<DoubleSolution>> create(Problem<DoubleSolution> problem) {
      Algorithm<List<DoubleSolution>> algorithm =
          new NSGAIIBuilder<DoubleSolution>(
                  problem,
                  new SBXCrossover(1.0, 20.0),
                  new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0),
                  100)
              .build();

      return algorithm;
    }
  }

  AlgorithmWrapper<DoubleSolution> a =
      (problem ->
          new NSGAIIBuilder<DoubleSolution>(
                  problem,
                  new SBXCrossover(1.0, 20.0),
                  new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0),
                  100)
              .build());

  public void run() {
    ExperimentSettings<DoubleSolution, List<DoubleSolution>> experimentSettings;

    List<ExperimentProblem<DoubleSolution>> problemList = new ArrayList<>();
    problemList.add(new ExperimentProblem<>(new ZDT1()));
    problemList.add(new ExperimentProblem<>(new ZDT2()));
    problemList.add(new ExperimentProblem<>(new ZDT3()));
    problemList.add(new ExperimentProblem<>(new ZDT4()));
    problemList.add(new ExperimentProblem<>(new ZDT6()));

    List<AlgorithmWrapper> algorithmWrappers = new ArrayList<>();

    // algorithmWrappers.add()

    List<AlgorithmBuilder<?>> algorithmBuilders = new ArrayList<>();

    String experimentName = "test";
    String experimentBaseDirectory = "";
    // experimentSettings = new ExperimentSettings<>(experimentName, experimentBaseDirectory, ) ;
  }
}
