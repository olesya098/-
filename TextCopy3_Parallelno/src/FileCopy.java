import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopy {

    // Метод для копирования файла
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024]; // Буфер для чтения данных
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length); // Записываем данные в выходной поток
            }
        } finally {
            if (is != null) {
                is.close(); // Закрываем входной поток
            }
            if (os != null) {
                os.close(); // Закрываем выходной поток
            }
        }
    }

    public static void main(String[] args) {
        // Укажите пути к файлам
        String file1 = "src/textС1.txt"; // Замените на фактический путь к первому файлу
        String file2 = "src/textС2.txt"; // Замените на фактический путь ко второму файлу
        String dest1 = "src/textCopyС1.txt"; // Путь для первой копии
        String dest2 = "src/textCopyС2.txt"; // Путь для второй копии

        File sourceFile1 = new File(file1);
        File sourceFile2 = new File(file2);
        File destinationFile1 = new File(dest1);
        File destinationFile2 = new File(dest2);

        // Создание потоков для параллельного копирования
        Thread copyThread1 = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis(); // Запоминаем время начала
                copyFileUsingStream(sourceFile1, destinationFile1); // Копируем первый файл
                long endTime = System.currentTimeMillis(); // Запоминаем время окончания
                long duration = endTime - startTime; // Вычисляем время выполнения
                System.out.println("Время копирования первого файла: " + duration + " мс");
            } catch (IOException e) {
                System.err.println("Ошибка при копировании первого файла: " + e.getMessage());
            }
        });

        Thread copyThread2 = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis(); // Запоминаем время начала
                copyFileUsingStream(sourceFile2, destinationFile2); // Копируем второй файл
                long endTime = System.currentTimeMillis(); // Запоминаем время окончания
                long duration = endTime - startTime; // Вычисляем время выполнения
                System.out.println("Время копирования второго файла: " + duration + " мс");
            } catch (IOException e) {
                System.err.println("Ошибка при копировании второго файла: " + e.getMessage());
            }
        });

        // Запуск потоков
        copyThread1.start();
        copyThread2.start();

        try {
            // Ожидание завершения обоих потоков
            copyThread1.join();
            copyThread2.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потоков: " + e.getMessage());
        }
    }
}
