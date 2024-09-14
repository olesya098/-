import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Ввод чисел
        System.out.println("Введите число a:");
        int a = in.nextInt();

        System.out.println("Введите множитель b:");
        int b = in.nextInt();

        // Выбор способа умножения
        System.out.println("Выберите способ умножения:");
        System.out.println("1. Умножение через сложение");
        System.out.println("2. Умножение через битовые операции");
        System.out.println("3. Умножение через массив");
        System.out.println("4. Рекурсивное умножение");
        int select = in.nextInt();
        int result = 0;
        switch (select){
            case 1:
                result = Summ(a,b);//умножения через сложение
                break;
            case 2:
                result = Bit(a,b);//умножения через битовые операции
                break;
            case 3:
                result = Massiv(a,b);//умножения через массив
                break;
            case 4:
                result = Recursion(a,b);//для рекурсивного умножения
                break;
            default:
                System.out.println("Некорректный выбор.");// Если выбор неправельный, выводим сообщение об ошибке
                return;
        }
        System.out.println("Результат: "+result);

    }
    // Метод 1: Умножение через сложение
    public static int Summ(int a, int b) {
        int result = 0;
        for (int i = 0; i < b; i++) {
            result += a;
        }
        return result;
    }
    public static int Bit (int a, int b){
        int result = 0;
        while (b > 0) {
            // Если b нечетное, добавляем a к результату
            if ((b & 1) == 1) {//Если b нечетное, то его последний бит равен 1
                //Если b — четное, то 0
                result += a;
            }
            // Умножаем a на 2 (сдвигаем влево)
            a <<= 1;
            // Делим b на 2 (сдвигаем вправо)
            b >>= 1;
        }
        return result;
    }
    //решение с помощью массива
    public static int Massiv (int a, int b){
        int result = 0;
        // массив для значений
        int[] mass = new int[b];
        for (int i = 0; i < b; i++) {
            mass[i] = a; // Запись  в массив
        }
        for (int i = 0; i < b; i++) {
            result += mass[i]; // Сложение значений в массиве
        }
        return result;

    }
    //решение рекурсией
    public static int Recursion(int a, int b) {
        // Базовый случай если b = 0, то a * 0 = 0
        if (b == 0) {
            return 0;
        }
        if (b < 0) {
            return -Recursion(a, -b); // Умножение на отрицательное число
        }
        // Рекурсивный случай добавляем a и уменьшаем b на 1
        return a + Recursion(a, b - 1);
    }


}
