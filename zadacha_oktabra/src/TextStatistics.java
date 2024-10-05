import java.io.*;

public class TextStatistics {

    public static void main(String[] args) {
        String inputFile = "src\\input.txt"; // Путь к файлу с текстом
        String outputFile = "src\\output.svc"; // Путь к выходному файлу

        try {
            // Чтение текста из файла
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            StringBuilder content = new StringBuilder();
            String line; // Переменная для хранения каждой строки
            int totalLines = 0; // Счетчик для подсчета общего количества строк

            // Читаем строки из файла до конца
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Добавляем строку к содержимому с переносом строки
                totalLines++; // Увеличиваем счетчик строк
            }
            reader.close(); // Закрываем BufferedReader после завершения чтения

            // Получаем текст в виде строки и убираем пробелы по краям
            String text = content.toString().trim();

            // Подсчет статистики
            int totalCharacters = text.length(); // Общее количество символов в тексте
            int Prob = text.replace(" ", "").length(); // Количество символов без пробелов
            // Подсчет пробелов
            int Count = text.length() - text.replace(" ", "").length();
            //длина строки-(заменяем пробелы пустотой)длина строки

            String[] words = text.trim().split("[\\s]+"); // Разделяем по пробелам
            // Подсчет общего количества слов
            int totalWords = words.length > 0 && !words[0].isEmpty() ? words.length : 0;
            //если длина первого слова > 0 и слово не пустая строка то возвращается длина слова иначе 0
            // Подсчет количества страниц (при условии, что на одной странице помещается 10 строк)
            int Pages = (int) Math.ceil((double) totalLines / 10); // Округляем вверх
            //Math.ceil округление
            // Подсчет количества абзацев, предполагая, что абзац разделен табуляцией
            int Paragraphs = text.split(" {4}").length; // Разделяем по четырем пробелам
            if (Paragraphs == 0 && totalLines > 0) {
                Paragraphs = 1; // Если текст не пустой, но абзацев нет, значит, один абзац.
            }

            // Вывод статистики в консоль
            System.out.println("Количество символов в тексте: " + totalCharacters);
            System.out.println("Количество символов без пробелов: " + Prob);
            System.out.println("Количество слов: " + totalWords);
            System.out.println("Количество страниц: " + Pages);
            System.out.println("Количество строк: " + totalLines); // Вывод количества строк
            System.out.println("Количество абзацев: " + Paragraphs); // Вывод количества абзацев

            // Запись статистики в файл
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write("Количество символов в тексте: " + totalCharacters + "\n");

                writer.write("Количество символов без пробелов: " + Prob + "\n");
                writer.write("Количество слов: " + totalWords + "\n");
                writer.write("Количество страниц: " + Pages + "\n");
                writer.write("Количество строк: " + totalLines + "\n"); // Запись количества строк
                writer.write("Количество абзацев: " + Paragraphs + "\n"); // Запись количества абзацев
                writer.close(); // Закрываем BufferedWriter

                System.out.println("Статистика успешно записана в файл " + outputFile);
            } catch (IOException e) {
                System.out.println("Ошибка записи в файл: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл не найден: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода: " + e.getMessage());

        } catch (Exception e) {
            // Обработка любых других исключений
            System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}
