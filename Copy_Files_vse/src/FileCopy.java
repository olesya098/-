import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class FileCopy {

    // Метод для копирования файла
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        // Открываем поток для чтения (source) и записи (dest)
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];// Буфер для чтения данных
            int length; // Переменная для хранения количества считанных байтов
            // Читаем данные из файла (source) и записываем их в файл (dest)
            while ((length = is.read(buffer)) > 0) {// Пока есть данные для чтения
                os.write(buffer, 0, length); // Записываем данные в файл назначения
            }
        }
    }

    // Метод для последовательного копирования двух файлов
    private static void sequentialCopy(File sFile1, File dFile1, File sFile2, File dFile2) {
        try {
            // Копируем первый файл
            copyFileUsingStream(sFile1, dFile1);
            // Копируем второй файл
            copyFileUsingStream(sFile2, dFile2);
            System.out.println("Последовательное копирование завершено!");
            System.out.println("Файл Posled скопирован в PosledCopy Файл Posled2 скопирован в PosledCopy2!");

        } catch (IOException e) {
            System.err.println("Ошибка при последовательном копировании: " + e.getMessage());
        }
    }

    // Метод для параллельного копирования двух файлов
    private static void parallelCopy(File sFile1, File dFile1, File sFile2, File dFile2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // Создаем пул потоков с двумя потоками
        try { // Запускаем оба задания параллельно
            executor.invokeAll(List.of(
                    Executors.callable(() -> {
                        try {
                            copyFileUsingStream(sFile1, dFile1); // Копируем первый файл
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    Executors.callable(() -> {
                        try {
                            copyFileUsingStream(sFile2, dFile2); // Копируем второй файл
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
            ));
            System.out.println("Параллельное копирование завершено!");
            System.out.println("Файл Parall скопирован в PosledCopy Файл Parall2 скопирован в PosledCopy2!");
        } catch (InterruptedException e) {
            System.err.println("Ошибка при параллельном копировании: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Создаем сканер для чтения вводимых данных
        // Выводим меню для выбора способа копирования

        System.out.println("Выберите способ копирования:");
        System.out.println("1. Однофайловое копирование");
        System.out.println("2. Последовательное копирование двух файлов");
        System.out.println("3. Параллельное копирование двух файлов");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1: {
                // Однофайловое копирование
                String source = "src/Prosto.txt";
                String dest = "src/ProstoCopy.txt";
                try {
                    copyFileUsingStream(new File(source), new File(dest));// Копируем файл
                    System.out.println("Файл Prosto успешно скопирован в ProstoCopy!");
                } catch (IOException e) {
                    System.err.println("Ошибка при копировании файла: " + e.getMessage());
                }
                break;
            }
            case 2: {
                // Последовательное копирование

                File sFile1 = new File("src/Posled.txt");
                File dFile1 = new File("src/PosledCopy.txt");
                File sFile2 = new File("src/Posled2.txt");
                File dFile2 = new File("src/PosledCopy2.txt");

                sequentialCopy(sFile1, dFile1, sFile2, dFile2);// Выполняем последовательное копирование
                break;
            }
            case 3: {
                // Параллельное копирование
                File sFile1 = new File("src/Parall.txt");
                File dFile1 = new File("src/PosledCopy.txt");
                File sFile2 = new File("src/Parall2.txt");
                File dFile2 = new File("src/PosledCopy2.txt");

                parallelCopy(sFile1, dFile1, sFile2, dFile2);// Выполняем параллельное копирование
                break;
            }
            default:
                System.out.println("Неверный выбор.");
                break;
        }
        scanner.close();
    }
}
