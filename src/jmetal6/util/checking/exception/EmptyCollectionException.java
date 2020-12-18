package jmetal6.util.checking.exception;

public class EmptyCollectionException extends RuntimeException {
  public EmptyCollectionException() {
    super("The collection is empty") ;
  }
}
