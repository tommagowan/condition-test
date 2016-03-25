package org.tcm;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IdBuffer {
    private final ArrayBlockingQueue<Long> buffer = new ArrayBlockingQueue<>(20);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition bufferEnabled = lock.newCondition();
    private volatile boolean enabled = true;


    public void start() {
        boolean interrupted = false;
        long id = 1;
        while (!interrupted) {
            try {
                if (!enabled) {
                    awaitBufferEnabled();
                }
                System.out.println("Putting " + id);
                buffer.put(id++);
                System.out.println("Putting " + id + " finished");
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void awaitBufferEnabled() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("awaitBufferEnabled");
            bufferEnabled.await();
            System.out.println("awaitBufferEnabled finished");
        } finally {
            lock.unlock();
        }
    }

    public void enable() {
        lock.lock();
        enabled = true;
        try {
            System.out.println("enable");
            bufferEnabled.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void disable() {
        System.out.println("disable");
        enabled = false;
    }

    public long getNext() {
        try {
            System.out.println("Getting...");
            Long next = buffer.take();
            System.out.println("Got " + next);
            return next;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
