package org.example.hw3;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Пул потоков.
 *
 * @author Evgeniy_Prokhorov
 */
public class CustomThreadPool {

    private final LinkedList<Runnable> taskQueue;
    private final Thread[] threads;
    private AtomicBoolean isShutdown = new AtomicBoolean(false);

    private final Object lock;

    public CustomThreadPool(final int poolSize) {
        if (poolSize <= 0) {
            throw new IllegalArgumentException("Размер пула должен быть больше 0");
        }
        lock = new Object();
        taskQueue = new LinkedList<>();
        threads = new Thread[poolSize];

        for (int i = 0; i < poolSize; i++) {
            threads[i] = initTread();
            threads[i].start();
        }
    }

    /**
     * Создает и возвращает поток, который обрабатывает задачи из очереди.
     *
     * @return Поток, который обрабатывает задачи.
     */
    private Thread initTread() {
        return new Thread(() -> {
            Runnable task;
            while (true) {
                if (isShutdown.get() && taskQueue.isEmpty()) {
                    return;
                }
                synchronized (lock) {
                    while (taskQueue.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Возникла ошибка при ожидании задач");
                        }
                    }
                    task = taskQueue.poll();
                    lock.notify();
                }
                try {
                    task.run();
                } catch (RuntimeException ex) {
                    System.out.println(ex.getMessage());
                    System.err.println("Возникла ошибка при выполнении задачи");
                }
            }
        });
    }


    /**
     * Выполняет переданную задачу в пуле потоков.
     *
     * @param task Задача, которую нужно выполнить.
     */
    public void execute(Runnable task) {
        if (isShutdown.get()) {
            throw new IllegalStateException("Не удается выполнить, потому что пул потоков был прерван");
        }

        synchronized (lock) {
            taskQueue.add(task);
            lock.notify();
        }
    }

    /**
     * Завершает работу пула потоков.
     */
    public void shutdown() {
        isShutdown.set(true);

        synchronized (lock) {
            while (!taskQueue.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Возникла ошибка при ожидании выполнения всех задач");
                }
            }
        }

        for (Thread thread: threads) {
            thread.interrupt();
        }
    }

}
