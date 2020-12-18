package jmetal6.example.multiobjective;

import java.io.FileNotFoundException;
import java.util.List;

import jmetal6.algorithm.Algorithm;
import jmetal6.algorithm.multiobjective.cellde.CellDE45;
import jmetal6.example.AlgorithmRunner;
import jmetal6.operator.crossover.impl.DifferentialEvolutionCrossover;
import jmetal6.operator.selection.SelectionOperator;
import jmetal6.operator.selection.impl.BinaryTournamentSelection;
import jmetal6.problem.Problem;
import jmetal6.solution.doublesolution.DoubleSolution;
import jmetal6.util.AbstractAlgorithmRunner;
import jmetal6.util.JMetalException;
import jmetal6.util.JMetalLogger;
import jmetal6.util.ProblemUtils;
import jmetal6.util.archive.impl.CrowdingDistanceArchive;
import jmetal6.util.comparator.RankingAndCrowdingDistanceComparator;
import jmetal6.util.evaluator.impl.SequentialSolutionListEvaluator;
import jmetal6.util.neighborhood.impl.C9;

/**
 * Class to configure and run the MOCell algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class CellDERunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException
   * Invoking command:
    java org.uma.jmetal.runner.multiobjective.MOCellRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
    DifferentialEvolutionCrossover crossover;
    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";
      referenceParetoFront = "referenceFronts/DTLZ1.3D.pf" ;
    }

    problem = ProblemUtils.<DoubleSolution> loadProblem(problemName);

    double cr = 0.5 ;
    double f = 0.5 ;

    crossover = new DifferentialEvolutionCrossover(cr, f, DifferentialEvolutionCrossover.DE_VARIANT.RAND_1_BIN) ;

    selection = new BinaryTournamentSelection<DoubleSolution>(new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    algorithm = new CellDE45(
        problem,
        50000,
        100,
        new CrowdingDistanceArchive<DoubleSolution>(100),
        new C9<DoubleSolution>((int)Math.sqrt(100), (int)Math.sqrt(100)),
        selection,
        crossover,
        20,
        new SequentialSolutionListEvaluator<DoubleSolution>()
     ) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute() ;

    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
}
