package org.tcm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        IdBuffer buffer = new IdBuffer();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(buffer::start);
        executorService.submit(()-> {while(true) {buffer.enable();Thread.sleep(10000);}});
        executorService.submit(()-> {while(true) {Thread.sleep(4000);buffer.disable();}});
        executorService.submit(()-> {while(true) {buffer.getNext();Thread.sleep(1000);}});
    }
}
