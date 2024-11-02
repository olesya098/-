import java.util.Random;

/**
 * Класс MaxSum предназначен для генерации массива случайных целых чисел
 * и вычисления максимальной суммы квадратов двух элементов из этого массива.
 */
public class MaxSum {

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        int number = 10000; // Количество случайных чисел для генерации
        int[] sequence = generateRandomNumbers(number); // Генерируем массив случайных целых чисел

        // Выводим все сгенерированные числа
        System.out.println("Сгенерированные случайные числа:");
        for (int i = 0; i < sequence.length; i++) {
            // Выводим число и пробел после него
            System.out.print(sequence[i] + " ");
            // Переходим на новую строку после каждых 20 чисел для лучшей читаемости
            if ((i + 1) % 20 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n"); // Добавляем пустую строку для разделения

        int result = maxSum(sequence); // Находим максимальную сумму квадратов двух элементов
        System.out.println("Максимальная сумма квадратов: " + result);
    }

    /**
     * Генерирует массив случайных целых чисел в диапазоне от -100 до 100.
     *
     * @param count количество случайных чисел для генерации
     * @return массив случайных целых чисел
     */
    private static int[] generateRandomNumbers(int count) {
        Random random = new Random();
        int[] numbers = new int[count]; // массив для хранения сгенерированных чисел

        for (int i = 0; i < count; i++) {
            // Генерируем случайные числа в диапазоне от -100 до 100
            numbers[i] = random.nextInt(201) - 100;
        }
        return numbers;
    }

    /**
     * Находит максимальную сумму квадратов двух элементов массива.
     * Учитываются только элементы, находящиеся не менее чем на 10 индексов
     * друг от друга.
     *
     * @param sequence массив целых чисел, в котором нужно найти максимальную сумму
     * @return максимальная сумма квадратов двух элементов
     */
    public static int maxSum(int[] sequence) {
        int n = sequence.length; // длина последовательности
        int maxSum = 0; // для хранения максимальной суммы

        // Поиск максимальной суммы двух квадратов
        for (int i = 0; i < n - 10; i++) { // Цикл по всем элементам, кроме последних 10
            for (int j = i + 10; j < n; j++) { // цикл, начиная с i + 10
                // Вычисляем сумму квадратов двух элементов
                int currentSum = (sequence[i] * sequence[i]) + (sequence[j] * sequence[j]);
                // Если текущая сумма больше максимальной, обновляем maxSum
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                }
            }
        }
        return maxSum;
    }
}