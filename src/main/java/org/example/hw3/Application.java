package org.example.hw3;

/**
 * Начало выполнения программы.
 *
 * @author Evgeniy_Prokhorov
 */
public class Application {
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(5);

        for (int i = 0; i < 10; i++) {
            int taskNumber = i;
            pool.execute(() -> {
                    System.out.println("Задача " + taskNumber + " выполняется по потоку " + Thread.currentThread().getName());
            });
        }

        pool.shutdown();

        System.out.println("All tasks finished");
    }
}
