package io.github.dfauth.aktr;

public interface Envelope<V> {

    <T> AktrRef<T> sender();

    V message();
}
