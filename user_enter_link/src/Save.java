import java.awt.Desktop;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Save {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Запрашиваем ссылку для скачивания
        System.out.print("Введите ссылку для скачивания файла: ");
        String fileURL = scanner.nextLine();

        // Путь для сохранения файла без расширения
        System.out.print("Введите путь для сохранения файла (например, G:/Системное программирование/Vse_save/file): ");
        String filePathWithoutExtension = scanner.nextLine();

        //поток для загрузки файла
        Thread download = new Thread(() -> downloadFile(fileURL, filePathWithoutExtension));

        // Запуск потока для загрузки файла
        download.start();

        // Ожидание завершения потока
        try {
            download.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потока: " + e.getMessage());
        }

        // Уведомление о завершении загрузки
        System.out.println("Загрузка файла завершена.");
    }

    // Метод для скачивания файла по указанному URL
    private static void downloadFile(String fileURL, String output) {
        try {
            URLConnection conn = new URL(fileURL).openConnection();
            InputStream is = conn.getInputStream();

            // Считываем первые 3 байта для определения расширения
            byte[] header = new byte[3];
            is.read(header);

            // Получаем расширение файла по заголовкам
            String extension = getExtension(header);
            if (extension != null) {
                output += extension; // Добавляем расширение к пути
                System.out.println("Определенное расширение файла: " + extension);
            } else {
                System.err.println("Не удалось определить расширение файла.");
                output += ".bin"; // Если расширение не определено, используем .bin
                System.out.println("Файл будет сохранен как: " + output);
            }

            // Создаем выходной поток с расширением
            OutputStream outstream = new FileOutputStream(new File(output));
            outstream.write(header); // Записываем заголовки в файл

            byte[] buffer = new byte[4096];
            int len;

            // Продолжаем скачивание оставшихся данных
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }

            outstream.close();
            is.close();
            System.out.println("Файл " + output + " успешно загружен.");
        } catch (IOException e) {
            System.err.println("Ошибка при скачивании: " + e.getMessage());
        }

        // Открытие загруженного файла
        openFile(output);
    }

    // Метод для определения расширения файла по заголовкам
    public static String getExtension(byte[] header) {
        Map<String, String> Extensions = new HashMap<>();
        Extensions.put("FF D8 FF", ".jpg");
        Extensions.put("89 50 4E", ".png");
        Extensions.put("49 44 33", ".mp3");
        Extensions.put("25 50 44", ".pdf");
        Extensions.put("00 00 00", ".mp4");
        Extensions.put("50 4B 03", ".docx");

        Extensions.put("47 49 46", ".gif");
        Extensions.put("50 4B 07", ".zip");
        Extensions.put("52 61 72", ".rar");

        // Преобразуем первые байты в строку шестнадцатиричных чисел
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < header.length; i++) {
            sb.append(String.format("%02X ", header[i])); // 16-тиричная система
        }

        // Преобразуем в строку для дальнейшей работы и удаляем пробел на конце
        String str = sb.toString().trim();

        // Возвращаем расширение, если нашли соответствие, если нет, то возвращаем null
        return Extensions.getOrDefault(str, null);
    }

    // Метод для открытия файла с использованием Desktop
    private static void openFile(String filePath) {
        try {
            File file = new File(filePath);
            // Проверяем, поддерживает ли текущая платформа функциональность Desktop
            if (Desktop.isDesktopSupported()) {
                // Если поддерживается, открываем файл в приложении по умолчанию, связанном с его типом
                Desktop.getDesktop().open(file);
            } else {
                System.err.println("Desktop не поддерживается.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при открытии файла " + filePath + ": " + e.getMessage());
        }
    }
}
