package io.github.dfauth.aktr;

import java.util.concurrent.CompletableFuture;

public interface AktrRef<V> {
    <T> CompletableFuture<T> ask(V v);

    void tell(V v);
}
