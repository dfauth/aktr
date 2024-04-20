package io.github.dfauth.aktr;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class TestCase {

    @Test
    public void testIt() throws ExecutionException, InterruptedException, TimeoutException {
        AktrRef<String> aktr1 = Aktr.create(1, e -> {
            e.sender().tell(
                    e.message().length());
        });
        CompletableFuture<Integer> f = aktr1.ask("test");
        assertEquals(4, f.get(100, TimeUnit.MILLISECONDS).intValue());
    }

    @Test
    public void testLookup() throws ExecutionException, InterruptedException, TimeoutException {
        AtomicInteger i = new AtomicInteger(0);
        AktrRef<Integer> aktr1 = Aktr.create(1, e -> {
            e.sender().tell(i.accumulateAndGet(e.message(), Math::addExact));
        });
        CompletableFuture<Integer> f = aktr1.ask(1);
        assertEquals(1, f.get(100, TimeUnit.MILLISECONDS).intValue());
    }
}
