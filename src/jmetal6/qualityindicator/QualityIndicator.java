package jmetal6.qualityindicator;

import java.io.Serializable;

import jmetal6.util.naming.DescribedEntity;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>

 * @param <Evaluate> Entity to runAlgorithm
 * @param <Result> Result of the evaluation
 */
public interface QualityIndicator<Evaluate, Result> extends DescribedEntity, Serializable {
  public Result evaluate(Evaluate evaluate) ;
}
