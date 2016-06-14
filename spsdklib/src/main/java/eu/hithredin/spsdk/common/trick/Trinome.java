package eu.hithredin.spsdk.common.trick;

/**
 * Like a pair,... but with three items
 */
public class Trinome<U,V,T> {
    public U first;
    public V second;
    public T third;

    public Trinome(U first, V second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
