import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.print("Введите размер массива ");
        Scanner scanner = new Scanner(System.in);
        int dlina = scanner.nextInt();
        int[] mass = new int[dlina];

        System.out.println("Введите элементы массива");
        for (int i = 0; i < dlina; i++) {
            mass[i] = scanner.nextInt();
        }
        // Запрашиваем степень мажоритарности в процентах
        System.out.println("Введите степень мажоритарности (в процентах) ");
        int a = scanner.nextInt();

        int o = 0;
        int m = 0;
// Поиск мажоритарного числа
        for (int i = 0; i < dlina; i++) {
            for (int j = 0; j < dlina; j++) {
                if (i != j) {
                    if (mass[i] == mass[j]) {
                        o += 1;
                    }
                }
            }
            // Проверяем, превышает ли количество вхождений порог мажоритарности
            if (o >= (dlina * a) / 100) {
                m = mass[i];
                i = dlina;
            } else {
                o = 0;
            }
        }


        System.out.println("Мажоритарное число " + m);
    }
}
