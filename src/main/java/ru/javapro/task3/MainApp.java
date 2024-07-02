package ru.javapro.task3;

public class MainApp {
    public static void main(String[] args) {
        Runnable command = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Run " + threadName);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("End " + threadName);
        };

        MyThreadPool myThreadPool = new MyThreadPool(4);

        for (int i = 0; i < 10; i++) {
            myThreadPool.execute(command);
        }

        myThreadPool.shutdown();

        myThreadPool.awaitTermination();

        System.out.println("End");
    }
}
