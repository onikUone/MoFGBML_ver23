package jmetal6.example.multiobjective;

import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.gde3.GDE3;
import jmetal6.algorithm.multiobjective.gde3.GDE3Builder;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.selection.impl.DifferentialEvolutionSelection;
import jmetal6.problem.doubleproblem.DoubleProblem;
import jmetal6.problem.multiobjective.cec2015OptBigDataCompetition.BigOpt2015;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.JMetalLogger;
import jmetal6.util.fileoutput.SolutionListOutput;
import jmetal6.util.fileoutput.impl.DefaultFileOutputContext;

/**
 * Class for configuring and running the GDE3 algorithm for solving a problem of the Big Optimization
 * competition at CEC2015
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class GDE3BigDataRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
   mvn
    -pl jmetal-exec
    exec:java -Dexec.mainClass="org.uma.jmetal.runner.multiobjective.GDE3BigDataRunner"
    -Dexec.args="[problemName]"
   */
  public static void main(String[] args) {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    DifferentialEvolutionSelection selection;
    DifferentialEvolutionCrossover crossover;

    String instanceName ;

    if (args.length == 1) {
      instanceName = args[0] ;
    } else {
      instanceName = "D12" ;
    }

    problem = new BigOpt2015(instanceName) ;

     /*
     * Alternatives:
     * - evaluator = new SequentialSolutionSetEvaluator()
     * - evaluator = new MultithreadedSolutionSetEvaluator(threads, problem)
     */

    double cr = 1.5 ;
    double f = 0.5 ;
    crossover = new DifferentialEvolutionCrossover(cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN) ;

    selection = new DifferentialEvolutionSelection() ;

    algorithm = new GDE3Builder(problem)
      .setCrossover(crossover)
      .setSelection(selection)
      .setMaxEvaluations(250000)
      .setPopulationSize(100)
      .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
      .execute() ;

    List<DoubleSolution> population = ((GDE3)algorithm).getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    new SolutionListOutput(population)
      .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
      .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
      .print();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }
}
