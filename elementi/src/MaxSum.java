import java.util.Random;

public class MaxSum {

    public static void main(String[] args) {
        int number = 10000; // Количество случайных чисел для генерации
        int[] sequence = Random(number); // Генерируем массив случайных целых чисел

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

    // Метод для генерации массива случайных целых чисел
    private static int[] Random(int count) {
        Random random = new Random();
        int[] numbers = new int[count]; // массив для хранения сгенерированных чисел

        for (int i = 0; i < count; i++) {
            // Генерируем случайные числа в диапазоне от -100 до 100
            numbers[i] = random.nextInt(201) - 100;
        }
        return numbers;
    }

    // Метод для нахождения максимальной суммы квадратов двух элементов
    public static int maxSum(int[] sequence) {
        int n = sequence.length; // длина последовательности
        int maxSum = 0; // для хранения максимальной суммы

        // Поиск максимальной суммы двух квадратов
        for (int i = 0; i < n - 10; i++) { // Цикл по всем элементам, кроме последних 10
            for (int j = i + 10; j < n; j++) { //цикл, начиная с i + 10
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
