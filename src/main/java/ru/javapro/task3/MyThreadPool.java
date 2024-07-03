package ru.javapro.task3;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool {
    private final LinkedList<Runnable> workList = new LinkedList<>();
    private volatile boolean isRunning = true;
    private volatile boolean isAddable = true;
    private final ReentrantLock mainLock = new ReentrantLock();
    private HashSet<Thread> threads = new HashSet<>();

    public MyThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            Thread thread = new Thread(new TaskWorker());
            threads.add(thread);
            thread.start();
        }
    }

    public void execute(Runnable command) {
        if (!isAddable) throw new IllegalStateException("Shutdowned");
        mainLock.lock();
        workList.offer(command);
        mainLock.unlock();
    }

    public void shutdown() {
        isAddable = false;
        isRunning = false;
    }

    public void awaitTermination() {
        isAddable = false;

        while (isRunning) {
            mainLock.lock();
            if (workList.size() == 0) {
                isRunning = false;
            }
            mainLock.unlock();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final class TaskWorker implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                mainLock.lock();
                Runnable nextTask = workList.poll();
                mainLock.unlock();
                if (nextTask != null) {
                    nextTask.run();
                }
            }

        }
    }

}
