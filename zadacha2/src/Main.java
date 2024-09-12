import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Размер последовательности
        final int ARRAY_SIZE = 1000;

        // Массив последовательности
        int[] array = new int[ARRAY_SIZE];

        // Генерация случайных чисел
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(10001); // Генерируем числа от 0 до 10000
        }

        // Минимальное число, кратное 21, являющееся произведением двух различных элементов
        int minNum = Integer.MAX_VALUE;
        boolean found = false;

        // Поиск минимального числа, кратного 21, являющегося произведением двух различных элементов
        for (int i = 0; i < ARRAY_SIZE; i++) {
            for (int j = i + 1; j < ARRAY_SIZE; j++) {
                int product = array[i] * array[j];
                if (product % 21 == 0 && product < minNum && isInArray(array, product)) {
                    minNum = product;
                    found = true;
                }
            }
        }

        // Вывод результата
        if (found) {
            System.out.println(minNum);
        } else {
            System.out.println(-1);
        }
    }

    // Вспомогательная функция, проверяющая, что число принадлежит массиву
    private static boolean isInArray(int[] array, int num) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == num) {
                return true;
            }
        }
        return false;
    }
}