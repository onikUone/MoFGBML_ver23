package jmetal6.util.checking.exception;

public class NullParameterException extends RuntimeException {
  public NullParameterException() {
    super("The parameter is null") ;
  }
}
