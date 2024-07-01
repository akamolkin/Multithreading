package ru.javapro.task3;

public class MainApp {
    public static void main(String[] args) {
        Runnable command = () -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task executed");
        };

        MyThreadPool myThreadPool = new MyThreadPool(4);

        for (int i = 0; i < 20; i++) {
            myThreadPool.execute(command);
        }

    //    myThreadPool.shutdown();

    //    myThreadPool.execute(command);

        myThreadPool.awaitTermination();

        System.out.println("End");
    }
}
