import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Copy {
    public static void main(String[] args) {
        //путь к файлу
        String file = "G:/Системное программирование/rrrr.txt";

        try {
            // Вызываем метод find для поиска максимальной длины последовательности
            int maxdlina = find(file);

            // Проверяем, нашли ли мы последовательности 'X'
            if (maxdlina > 0) {
                // Если нашли, выводим длину
                System.out.println("Длина самой длинной последовательности 'X': " + maxdlina);
            } else {
                // Если нет
                System.out.println("В файле нет символов 'X'.");
            }
        } catch (IOException e) {
            // Обработка исключений при чтении файла
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    // Метод для поиска максимальной длины последовательности
    private static int find(String filePath) throws IOException {
        int maxD = 0; // максимальная длина последовательности
        int tekysha_dlina = 0; // текущая длина последовательности

        // Открываем для чтения файла
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int tekysha_simvol; // текущий символ
            // Читаем пока не достигнем конца файла
            while ((tekysha_simvol = br.read()) != -1) {
                char c = (char) tekysha_simvol; // Преобразуем в char
                if (c == 'X') { // равно ли 'X'
                    tekysha_dlina++; // Увеличиваем текущую длину последовательности
                    // Если текущая длина > максимальной, обновляем максимальную длину
                    if (tekysha_dlina > maxD) {
                        maxD = tekysha_dlina;
                    }
                } else {
                    tekysha_dlina = 0; //если другой символ - сброс
                }
            }
        }

        // Возвращаем максимальную длину 
        return maxD;
    }
}
