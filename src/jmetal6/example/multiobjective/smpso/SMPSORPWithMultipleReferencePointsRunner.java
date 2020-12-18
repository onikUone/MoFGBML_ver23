package jmetal6.example.multiobjective.smpso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.smpso.SMPSORP;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.mutation.MutationOperator;
import jmetal6.operator.mutation.impl.PolynomialMutation;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.archivewithreferencepoint.ArchiveWithReferencePoint;
import jmetal6.util.archivewithreferencepoint.impl.CrowdingDistanceArchiveWithReferencePoint;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

public class SMPSORPWithMultipleReferencePointsRunner {
  /**
   * Program to run the SMPSORP algorithm with two reference points. SMPSORP is described in "Extending
   * the Speed-constrained Multi-Objective PSO (SMPSO) With Reference Point Based Preference
   * Articulation. Antonio J. Nebro, Juan J. Durillo, José García-Nieto, Cristóbal Barba-González,
   * Javier Del Ser, Carlos A. Coello Coello, Antonio Benítez-Hidalgo, José F. Aldana-Montes.
   * Parallel Problem Solving from Nature -- PPSN XV. Lecture Notes In Computer Science, Vol. 11101,
   *  pp. 298-310. 2018"
   *
   * @author Antonio J. Nebro
   */
  public static void main(String[] args) throws JMetalException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    MutationOperator<DoubleSolution> mutation;

    String problemName;
    if (args.length == 1) {
      problemName = args[0];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT2";
    }
    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    List<List<Double>> referencePoints;
    referencePoints = new ArrayList<>();
    referencePoints.add(Arrays.asList(0.2, 0.8));
    referencePoints.add(Arrays.asList(0.7, 0.4));

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    int maxIterations = 250;
    int swarmSize = 100;

    List<ArchiveWithReferencePoint<DoubleSolution>> archivesWithReferencePoints = new ArrayList<>();

    for (int i = 0; i < referencePoints.size(); i++) {
      archivesWithReferencePoints.add(
              new CrowdingDistanceArchiveWithReferencePoint<DoubleSolution>(
                      swarmSize / referencePoints.size(), referencePoints.get(i)));
    }

    algorithm = new SMPSORP(problem,
            swarmSize,
            archivesWithReferencePoints,
            referencePoints,
            mutation,
            maxIterations,
            0.0, 1.0,
            0.0, 1.0,
            2.5, 1.5,
            2.5, 1.5,
            0.1, 0.1,
            -1.0, -1.0,
            new SequentialSolutionListEvaluator<>());

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    new SolutionListOutput(population)
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    for (int i = 0; i < archivesWithReferencePoints.size(); i++) {
      new SolutionListOutput(archivesWithReferencePoints.get(i).getSolutionList())
              .setVarFileOutputContext(new DefaultFileOutputContext("VAR" + i + ".tsv"))
              .setFunFileOutputContext(new DefaultFileOutputContext("FUN" + i + ".tsv"))
              .print();
    }

    System.exit(0);
  }
}
