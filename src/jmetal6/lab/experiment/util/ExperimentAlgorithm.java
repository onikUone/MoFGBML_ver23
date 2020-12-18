package jmetal6.lab.experiment.util;

import java.io.File;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.lab.experiment.Experiment;
import jmetal6.solution.Solution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class defining tasks for the execution of algorithms in parallel.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExperimentAlgorithm<S extends Solution<?>, Result extends List<S>>  {
  private Algorithm<Result> algorithm;
  private String algorithmTag;
  private String problemTag;
  private int runId ;

  /**
   * Constructor
   */
  public ExperimentAlgorithm(
          Algorithm<Result> algorithm,
          String algorithmTag,
          ExperimentProblem<S> problem,
          int runId) {
    this.algorithm = algorithm;
    this.algorithmTag = algorithmTag;
    this.problemTag = problem.getTag();
    this.runId = runId ;
  }

  public ExperimentAlgorithm(
          Algorithm<Result> algorithm,
          ExperimentProblem<S> problem,
          int runId) {

    this(algorithm,algorithm.getName(),problem,runId);

  }

  public void runAlgorithm(Experiment<?, ?> experimentData) {
    String outputDirectoryName = experimentData.getExperimentBaseDirectory()
            + "/data/"
            + algorithmTag
            + "/"
            + problemTag;

    File outputDirectory = new File(outputDirectoryName);
    if (!outputDirectory.exists()) {
      boolean result = new File(outputDirectoryName).mkdirs();
      if (result) {
        JMetalLogger.logger.info("Creating " + outputDirectoryName);
      } else {
        JMetalLogger.logger.severe("Creating " + outputDirectoryName + " failed");
      }
    }

    String funFile = outputDirectoryName + "/FUN" + runId + ".tsv";
    String varFile = outputDirectoryName + "/VAR" + runId + ".tsv";
    JMetalLogger.logger.info(
            " Running algorithm: " + algorithmTag +
                    ", problem: " + problemTag +
                    ", run: " + runId +
                    ", funFile: " + funFile);


    algorithm.run();
    Result population = algorithm.getResult();

    new SolutionListOutput(population)
            .setVarFileOutputContext(new DefaultFileOutputContext(varFile))
            .setFunFileOutputContext(new DefaultFileOutputContext(funFile))
            .print();
  }

  public Algorithm<Result> getAlgorithm() {
    return algorithm;
  }

  public String getAlgorithmTag() {
    return algorithmTag;
  }

  public String getProblemTag() {
    return problemTag;
  }

  public int getRunId() { return this.runId;}
}
