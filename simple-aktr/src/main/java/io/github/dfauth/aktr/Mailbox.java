package io.github.dfauth.aktr;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class Mailbox<V> extends TimerTask {

    private final Function<AktrContext, Consumer<Envelope<V>>> f;
    private ArrayBlockingQueue<Runnable> q = new ArrayBlockingQueue<>(10);
    private Timer timer = new Timer();

    public Mailbox(Function<AktrContext, Consumer<Envelope<V>>> f) {
        this.f = f;
    }

    void enqueue(Envelope<V> envelope) {
        this.q.offer(() -> f.apply(null).accept(envelope));
    }

    @Override
    public void run() {
        Runnable r;
        while((r = poll())!= null) {
            r.run();
        }
        timer.schedule(this, 100);
    }

    private Runnable poll() {
        try {
            return q.poll(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

}
