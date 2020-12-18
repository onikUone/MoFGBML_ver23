package jmetal6.util.measure.impl;

import jmetal6.util.measure.PullMeasure;
import jmetal6.util.measure.PushMeasure;

/**
 * A {@link BasicMeasure} provides a simple way to define a
 * measure that merely stores a single value
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class BasicMeasure<T> extends SimplePushMeasure<T> implements
    PullMeasure<T>, PushMeasure<T> {

  private T value ;

  /**
   * Create a {@link BasicMeasure}
   */
  public BasicMeasure() {
    super(
        "SingleValue",
        "Generic measure which stores a single value.");
  }

  /**
   * @param value The value to be stored
   */
  public synchronized void set(T value) {
    this.value = value ;
  }

  /**
   * @return the current value
   */
  @Override
  public synchronized T get() {
    return value;
  }

  //TODO: invoke push in set()???
  @Override public void push (T value) {
    super.push(value) ;
  }
}