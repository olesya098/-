import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Размер массива
        final int ARRAY_SIZE = 1000;

        // Массив чисел
        int[] num = new int[ARRAY_SIZE];

        // Генерация случайных чисел
        Random random = new Random();

        // массив случайных чисел от 0 до 1000
        for (int i = 0; i < ARRAY_SIZE; i++) {
            num[i] = random.nextInt(1000); // Генерируем случайное число от 0 до 999
        }

        // Минимальное число, кратное 3
        int min3 = Integer.MAX_VALUE;
        // Минимальное число, кратное 7
        int min7 = Integer.MAX_VALUE;
        // Минимальное число, кратное 21
        int min21 = Integer.MAX_VALUE;
        // Минимальное число, не кратное 3, 7 и 21
        int min = Integer.MAX_VALUE;

        // Поиск минимальных чисел
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (num[i] % 21 == 0 && num[i] < min21) min21 = num[i];
            else if (num[i] % 3 == 0 && num[i] < min3) min3 = num[i];//Аналогично что и в 7
            else if (num[i] % 7 == 0 && num[i] < min7) min7 = num[i];//Проверяет, делится ли элемент на 7 без остатка. Если делится, и этот элемент меньше текущего минимального значения min7, то обновляет min7 на этот элемент.
            else if (num[i] < min) min = num[i];
        }

        int result1 = 0; // значения, которые не найдены
        int result2 = 0;

        // Находим два возможных ответа кратное 3* кратное 7
        if (min3 != Integer.MAX_VALUE && min7 != Integer.MAX_VALUE)
            result1 = min3 * min7;

        // Минимальное число, кратное 21 * мин число
        if (min21 != Integer.MAX_VALUE)
            result2 = min21 * Math.min(min3, Math.min(min7, min));// меньшее  между min7 и min.
        // сравнивается с min3 и минимальное значение из трех переменных
        // result2  = (мин знач /21)*(мин знач / 7 / 3)

        int itog = Math.min(result1, result2);

        if (itog != -1)
            System.out.println(itog);
        else
            System.out.println(-1);
    }
}
