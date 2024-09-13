package org.example;

import org.example.processor.TestRunner;
import org.example.service.ServiceTest;

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
