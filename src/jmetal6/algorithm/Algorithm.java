package jmetal6.algorithm;

import java.io.Serializable;

import jmetal6.util.naming.DescribedEntity;

/**
 * Interface representing an algorithm
 * @author Antonio J. Nebro
 * @version 0.1
 * @param <Result> Result
 */
public interface Algorithm<Result> extends Runnable, Serializable, DescribedEntity {
  void run() ;
  Result getResult() ;
}
