package org.example;

import org.example.hw1.processor.TestRunner;
import org.example.hw1.service.ServiceTest;

/**
 * Начало выполнения программы.
 *
 * @author Evgeniy_Prokhorov
 */
public class Application {
    public static void main(String[] args) {
        TestRunner.runTests(ServiceTest.class);
    }
}
