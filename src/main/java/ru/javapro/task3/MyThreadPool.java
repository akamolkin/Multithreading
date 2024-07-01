package ru.javapro.task3;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool {
   // private final List<Runnable> workQueue = new LinkedList<>();
    private final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
    private volatile boolean isRunning = true;
    private ThreadPoolExecutor threadPoolExecutor;
    private final ReentrantLock mainLock = new ReentrantLock();

    public MyThreadPool(int poolSize) {
        threadPoolExecutor = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                10L,
                TimeUnit.SECONDS,
                workQueue,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // завершать core потоки по истечении таймаута
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        // Запускаем предварительно потоки
        threadPoolExecutor.prestartAllCoreThreads();
    }

    public void execute(Runnable command) {
        if (!isRunning) throw new IllegalStateException("Shutdowned");
        workQueue.offer(command);
    }

    public void shutdown() {
        isRunning = false;
    }

    public void awaitTermination() {
        mainLock.lock();
        try {
            while (threadPoolExecutor.getActiveCount() > 0 ){

            }
        //    System.out.println("Active "+ threadPoolExecutor.getActiveCount());
            threadPoolExecutor.shutdown();
         //   return true;
        } finally {
            mainLock.unlock();
        }
    }
}
