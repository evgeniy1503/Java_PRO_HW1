package org.example.hw1.processor;

import org.example.hw1.annotations.AfterSuite;
import org.example.hw1.annotations.AfterTest;
import org.example.hw1.annotations.BeforeSuite;
import org.example.hw1.annotations.BeforeTest;
import org.example.hw1.annotations.CsvSource;
import org.example.hw1.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для обрабатывающий аннотации.
 *
 * @author Evgeniy_Prokhorov
 */
public class TestRunner {

    public static List<Method> beforeTestMethods = new ArrayList<>();
    public static List<Method> afterTestMethods = new ArrayList<>();
    public static List<Method> csvSourceMethods = new ArrayList<>();
    public static List<Method> testMethods = new ArrayList<>();
    public static List<Method> afterSuiteMethods = new ArrayList<>();
    public static List<Method> beforeSuiteMethods = new ArrayList<>();

    public static void runTests(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            handleMethodAnnotations(method);
        }
        Object object;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            object = constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось получить конструктор у класса: " + clazz.getSimpleName());
        }
        preInvokeMethod();
        invokeMethod(object);

    }

    /**
     * Метод по обработке аннотаций, установленных над методом
     *
     * @param method обрабатываемый метод
     */
    public static void handleMethodAnnotations(Method method) {
        if (method.isAnnotationPresent(BeforeTest.class)) {
            beforeTestMethods.add(method);
        } else if (method.isAnnotationPresent(AfterTest.class)) {
            afterTestMethods.add(method);
        } else if (method.isAnnotationPresent(CsvSource.class)) {
            csvSourceMethods.add(method);
        } else if (method.isAnnotationPresent(Test.class)) {
            validateTestAnnotation(method.getAnnotation(Test.class));
            testMethods.add(method);
        } else if (method.isAnnotationPresent(AfterSuite.class)) {
            validateSuiteAnnotation(method);
            afterSuiteMethods.add(method);
        } else if (method.isAnnotationPresent(BeforeSuite.class)) {
            validateSuiteAnnotation(method);
            beforeSuiteMethods.add(method);
        } else {
            throw new RuntimeException("Указана не обрабатываемая аннотация над методом: " + method.getName());
        }
    }

    /**
     * Метод проверки аннотации @Test
     *
     * @param annotation Test аннотация
     */
    public static void validateTestAnnotation(Test annotation) {
        if (annotation.priority() < 1 || annotation.priority() > 10) {
            throw new IllegalArgumentException("Указано значение приоритета, которое не попадает в диапазон от 1 до 10");
        }
    }

    /**
     * Метод проверки аннотации @AfterSuite и @BeforeSuite
     *
     * @param method обрабатываемый метод
     */
    public static void validateSuiteAnnotation(Method method) {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Аннотация не может стоять над не статическим методом");
        }
        if (method.isAnnotationPresent(AfterSuite.class) && !afterSuiteMethods.isEmpty()) {
            throw new IllegalArgumentException("Аннотация @AfterSuite не может стоять над несколькими методами");
        }
        if (method.isAnnotationPresent(BeforeSuite.class) && !beforeSuiteMethods.isEmpty()) {
            throw new IllegalArgumentException("Аннотация @BeforeSuite не может стоять над несколькими методами");
        }
    }

    /**
     * Метод для настройки выполнения методов
     */
    public static void preInvokeMethod() {
        testMethods.sort((method1, method2) -> method2.getAnnotation(Test.class).priority() - method1.getAnnotation(Test.class).priority());

    }

    /**
     * Порядок выполнения методов
     *
     * @param object объект, на котором будут выполняться методы
     */
    public static void invokeMethod(Object object) {
        runMethods(beforeSuiteMethods, object);
        for(Method method : testMethods) {
            runMethods(beforeTestMethods, object);
            runMethod(method, object);
            runMethods(afterTestMethods, object);
        }
        runMethodsWithParams(csvSourceMethods, object);
        runMethods(afterSuiteMethods, object);
    }

    /**
     * Выполнение списка методов на заданном объекте.
     *
     * @param methods список методов, которые необходимо выполнить
     * @param object объект, на котором будут выполняться методы
     */
    public static void runMethods(List<Method> methods,
                                  Object object) {
       if (!methods.isEmpty()) {
           for (Method method : methods) {
               try {
                   method.invoke(object);
               } catch (Exception e) {
                   throw new RuntimeException("Возникла ошибка при выполнении метода: " + method.getName());
               }
           }
       }
    }

    /**
     * Выполнение метода на заданном объекте.
     *
     * @param method метод, которые необходимо выполнить
     * @param object объект, на котором будут выполняться методы
     */
    public static void runMethod(Method method,
                                 Object object) {
        try {
            method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException("Возникла ошибка при выполнении метода: " + method.getName());
        }
    }

    /**
     * Выполнение списка методов на заданном объекте с параметрами.
     *
     * @param methods список методов, которые необходимо выполнить
     * @param object объект, на котором будут выполняться методы
     */
    public static void runMethodsWithParams(List<Method> methods,
                                            Object object) {
        if (!methods.isEmpty()) {
            for (Method method : methods) {
                CsvSource annotation = method.getAnnotation(CsvSource.class);
                String[] strings = annotation.value().split(", ");
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] args = new Object[parameterTypes.length];
                for (int i = 0; i < strings.length; i++) {
                    args[i] = castToType(parameterTypes[i], strings[i].trim());
                }
                try {
                    method.invoke(object, args);
                } catch (Exception e) {
                    throw new RuntimeException("Возникла ошибка при выполнении метода: " + method.getName());
                }
            }
        }
    }

    /**
     * Преобразует строковое значение в объект заданного типа.
     *
     * @param paramType класс типа, в который необходимо преобразовать значение.
     * @param value строковое значение, которое нужно преобразовать.
     * @return преобразованное значение в виде объекта заданного типа.
     */
    private static Object castToType(Class<?> paramType,
                                     String value) {
        Object result;
        if (String.class.equals(paramType)) {
            result = value;
        } else if (Boolean.class.equals(paramType) || boolean.class.equals(paramType)) {
            result = Boolean.parseBoolean(value);
        } else if (Integer.class.equals(paramType) || int.class.equals(paramType)) {
            result = Integer.parseInt(value);
        } else if (Long.class.equals(paramType) || long.class.equals(paramType)) {
            result = Long.parseLong(value);
        } else if (Float.class.equals(paramType) || float.class.equals(paramType)) {
            result = Float.parseFloat(value);
        } else if (Double.class.equals(paramType) || double.class.equals(paramType)) {
            result = Double.parseDouble(value);
        } else {
            throw new RuntimeException("Указан не поддерживаемый тип данных");
        }
        return result;
    }

}