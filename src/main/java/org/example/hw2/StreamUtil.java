package org.example.hw2;

import org.example.hw2.entity.Employee;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Утилитарный класс для работы со списком и массивами.
 *
 * @author Evgeniy_Prokhorov
 */
public class StreamUtil {

    public static List<Employee> employeeList = List.of(
            new Employee("Евгений", 33, "Java-разработчик"),
            new Employee("Ирина", 25, "HR"),
            new Employee("Олег", 39, "Тестировщик"),
            new Employee("Карина", 32, "Дизайнер"),
            new Employee("Семен", 23, "Инженер"),
            new Employee("Павел", 45, "Инженер"),
            new Employee("Илья", 64, "Инженер"),
            new Employee("Сергей", 37, "Инженер")
    );

    /**
     * Метод удаляет дубликаты чисел
     */
    public static void removingDuplicates() {
        List<Integer> integerList = List.of(1,2,3,4,5,6,2,3);

        List<Integer> result = integerList.stream().distinct().toList();

        System.out.println(result);
    }

    /**
     * Найдите в списке целых чисел 3-е наибольшее число
     */
    public static void findThirdLargestNumber() {
        List<Integer> integerList = List.of(5, 2, 10, 9, 4, 3, 10, 1, 13);

        Integer result = integerList.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow();

        System.out.println(result);
    }

    /**
     * Найдите в списке целых чисел 3-е наибольшее «уникальное» число
     */
    public static void findThirdUniqueLargestNumber() {
        List<Integer> integerList = List.of(5, 2, 10, 9, 4, 3, 10, 1, 13);

        Integer result = integerList.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()
                .skip(2)
                .findFirst()
                .orElseThrow();

        System.out.println(result);
    }

    /**
     * Получить список имен 3 самых старших сотрудников с должностью «Инженер», в порядке убывания возраста
     */
    public static void getThreeOldestEngineersByAge() {
        List<Employee> result = employeeList.stream()
                .filter(employee -> employee.getPosition().equals("Инженер"))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .limit(3)
                .toList();

        System.out.println(result);
    }

    /**
     * Посчитать средний возраст сотрудников с должностью «Инженер»
     */
    public static void calculateAverageAgeByRole() {
        Double result = employeeList.stream()
                .filter(employee -> employee.getPosition().equals("Инженер"))
                .collect(Collectors.averagingInt(Employee::getAge));

        System.out.println(result);
    }

    /**
     * Найдите в списке слов самое длинное
     */
    public static void findLongestWord() {
        List<String> words = List.of("Мама", "Почта", "Ипотека", "Яма", "Длиношеее");

        String result = words.stream()
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();

        System.out.println(result);
    }

    /**
     * Постройте хеш-мапы, в которой будут хранится пары: слово - сколько раз оно встречается во входной строке
     */
    public static void buildMap() {
        String text = "солнце книга озеро смех мечта ветер радуга камень книга смех танец свобода смех";

        Map<String, Long> result = Arrays.stream(text.split(" "))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        System.out.println(result);
    }

    /**
     * Отпечатайте в консоль строки из списка в порядке увеличения длины слова
     */
    public static void sortWordsByLength() {
        String text = "apple, banana, cherry, date, grape, kiwi, lemon, mango, orange, pear";

        List<String> result = Arrays.stream(text.split(", "))
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .toList();

        System.out.println(result);
    }

    /**
     * Найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
     */
    public static void longestWord() {
        String[] texts = {
           "программа машина юзер диск память",
           "озеро лес трава куст небо",
           "фонарь улица забор парк сосед"
        };

        String result = Arrays.stream(texts)
                .flatMap(text -> Arrays.stream(text.split(" ")))
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();

        System.out.println(result);

    }
}
