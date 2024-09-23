package org.example.service;

import org.example.annotations.AfterSuite;
import org.example.annotations.AfterTest;
import org.example.annotations.BeforeSuite;
import org.example.annotations.BeforeTest;
import org.example.annotations.CsvSource;
import org.example.annotations.Test;

/**
 * Сервис выполняющий работу с аннотациями.
 *
 * @author Evgeniy_Prokhorov
 */
public class ServiceTest {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("Метод помеченный аннотацией @BeforeSuite выполнился.");
    }

    @AfterSuite
    public static void afterSuite() {
        System.out.println("Метод помеченный аннотацией @AfterSuite выполнился.");
    }

    @BeforeTest
    public static void beforeTest() {
        System.out.println("Метод помеченный аннотацией @BeforeTest выполнился перед каждым тестом.");
    }

    @AfterTest
    public static void afterTest() {
        System.out.println("Метод помеченный аннотацией @AfterTest выполнился после каждого тестом.");
    }

    @Test(priority = 1)
    public void firstTest() {
        System.out.println("Первый тест выполнен.");
    }

    @Test(priority = 2)
    public void secondTest() {
        System.out.println("Второй тест выполнен.");
    }

    @Test(priority = 3)
    public void thirdTest() {
        System.out.println("Третий тест выполнен.");
    }

    @Test(priority = 10)
    public void fifthTest() {
        System.out.println("Пятый тест выполнен.");
    }

    @Test(priority = 4)
    public void fourthTest() {
        System.out.println("Четвертый тест выполнен.");
    }

    @CsvSource("10, Java, 20, true")
    public void csvSourceFirst(int a, String b, int c, boolean d) {
        System.out.println("Аргументы метода: " + a + " " + b + " " + c + " " + d);
    }

    @CsvSource("PHP, 17, 0.33")
    public void csvSourceSecond(String a, int b, double c) {
        System.out.println("Аргументы метода: " + a + " " + b + " " + c);
    }
}
