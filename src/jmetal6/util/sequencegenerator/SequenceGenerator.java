package jmetal6.util.sequencegenerator;

public interface SequenceGenerator<T> {
    T getValue() ;
    void generateNext() ;
    int getSequenceLength() ;
}
