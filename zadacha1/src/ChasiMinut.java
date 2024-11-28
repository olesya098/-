import java.util.Scanner;

public class ChasiMinut {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем начальное время в формате часы минуты
        String[] startTime = scanner.nextLine().split(" "); // Читаем строку и разбиваем её на часы и минуты
        int startChas = Integer.parseInt(startTime[0]); // Преобразуем часы в целое число
        int startMinute = Integer.parseInt(startTime[1]); // Преобразуем минуты в целое число

        // Считываем конечное время в формате "часы минуты"
        String[] endTime = scanner.nextLine().split(" ");
        int endChas = Integer.parseInt(endTime[0]);
        int endMinute = Integer.parseInt(endTime[1]);

        // Вычисляем общее количество ударов
        int strikes = VseYdari(startChas, startMinute, endChas, endMinute);
        System.out.println(strikes); // Вывод результата
        scanner.close();
    }

    // Метод для расчета количества ударов
    public static int VseYdari(int startChas, int startMinute, int endChas, int endMinute) {
        int vseYdari = 0; // Переменная для хранения общего количества ударов

        // Преобразуем время в минуты с начала суток
        int startVseMinutes = startChas * 60 + startMinute; //общее количество минут начала
        int endVseMinutes = endChas * 60 + endMinute; // общее количество минут конца

        // Если конечное время меньше начального, добавляем 24 часа к конечному времени
        if (endVseMinutes <= startVseMinutes) {
            endVseMinutes += 24 * 60; // Переводим в минуты
        }

        // Проходим по каждому часу в интервале
        int TecusheeTime = startVseMinutes; // Устанавливаем текущее время равным времени начала
        while (TecusheeTime < endVseMinutes) { // Пока текущее время меньше конечного
            int TecusheeHour = (TecusheeTime / 60) % 24; // Получаем текущий час в 24 формате
            int TecusheeMinute = TecusheeTime % 60; // Получаем текущие минуты

            // Преобразуем текущий час в 12 формат
            int hour12 = TecusheeHour % 12; // Переводим в 12 формат
            if (hour12 == 0) hour12 = 12; // Если час равен 0, то устанавливаем его равным 12

            // Проверяем, происходит ли удар в полночь
            if (TecusheeMinute == 0) {
                vseYdari += hour12; // Добавляем количество ударов
            }
            // Проверяем удар в полчаса (30 минут)
            else if (TecusheeMinute == 30) {
                vseYdari += 1; // Добавляем один удар
            }

            TecusheeTime += 1; // Переходим к следующей минуте
        }

        return vseYdari; // Возвращаем общее количество ударов
    }
}
