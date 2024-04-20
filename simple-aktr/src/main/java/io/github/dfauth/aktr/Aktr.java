package io.github.dfauth.aktr;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;

public class Aktr {

    public static <K,V> AktrRef<V> create(K k, Consumer<Envelope<V>> consumer) {
        AktrRefImpl<V> aktrRef = new AktrRefImpl<>(consumer);
        ForkJoinPool.commonPool().submit(aktrRef.process());
        return aktrRef;
    }

    public static <K,V> AktrRef<V> create(K k, Function<AktrContext, Consumer<Envelope<V>>> f) {
        AktrRefImpl<V> aktrRef = new AktrRefImpl<>(f);
        ForkJoinPool.commonPool().submit(aktrRef.process());
        return aktrRef;
    }
}
