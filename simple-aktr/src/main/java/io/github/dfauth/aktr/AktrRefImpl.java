package io.github.dfauth.aktr;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class AktrRefImpl<V> implements AktrRef<V> {

    private Mailbox mailbox;

    public AktrRefImpl(Function<AktrContext, Consumer<Envelope<V>>> f) {
        this.mailbox = new Mailbox(f);
    }

    public AktrRefImpl(Consumer<Envelope<V>> consumer) {
        this.mailbox = new Mailbox(ignored -> consumer);
    }

    @Override
    public void tell(V v) {
        mailbox.enqueue(envelope(v));
    }

    @Override
    public <T> CompletableFuture<T> ask(V v) {
        CompletableFuture<T> f = new CompletableFuture<>();
        mailbox.enqueue(envelope(v, f));
        return f;
    }


    Envelope<V> envelope(V v) {
        return envelope(v, new CompletableFuture<>());
    }

    <T> Envelope<V> envelope(V v, CompletableFuture<T> f) {
        return new Envelope<>() {

            @Override
            public AktrRef<T> sender() {
                return new AktrRef<>() {
                    @Override
                    public <R> CompletableFuture<R> ask(T t) {
                        return null;
                    }

                    @Override
                    public void tell(T t) {
                        f.complete(t);
                    }
                };
            }

            @Override
            public V message() {
                return v;
            }
        };
    }

    public Runnable process() {
        return mailbox;
    }
}
