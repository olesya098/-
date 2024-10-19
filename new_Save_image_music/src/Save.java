import java.awt.Desktop;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Save {

    public static void main(String[] args) {
        String imagePath = "G:/Системное программирование/Vse_save/image.jpg";
        String audioPath = "G:/Системное программирование/Vse_save/file.mp3";
        String videoPath = "G:/Системное программирование/Vse_save/video.mp4";

        // Создаем поток для загрузки изображения JPG
        Thread imageD = new Thread(() -> downloadFile(
                "https://avatars.mds.yandex.net/i?id=8464aa3137d94d480ccb575a71dd28754b344b0f-4590689-images-thumbs&n=13",
                imagePath));

        // Создаем поток для загрузки аудиофайла
        Thread audioD = new Thread(() -> downloadFile(
                "https://rus.hitmotop.com/get/music/20230331/Kravc_Gio_Pika_-_Gde_proshla_ty_75704918.mp3",
                audioPath));

        // Создаем поток для загрузки видеофайла
        Thread videoD = new Thread(() -> downloadFile(
                "https://v3.cdnpk.net/videvo_files/video/free/2012-11/large_preview/MVI_1482.mp4?token=exp=1729349403~hmac=06c566924f5b1aad7fa947676b47d7e7ddd4d97f97923d398a44221eaafb49c0",
                videoPath));

        // Запуск потоков для параллельной загрузки файлов
        imageD.start();
        audioD.start();
        videoD.start();

        // Ожидание завершения каждого потока
        try {
            imageD.join();
            audioD.join();
            videoD.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потока: " + e.getMessage());
        }

        // Уведомление о завершении загрузки всех файлов
        System.out.println("Загрузка файлов завершена.");

        // Открытие загруженных файлов
        openFile(imagePath);
        openFile(audioPath);
        openFile(videoPath);
    }

    // Метод для скачивания файла по указанному URL
    private static void downloadFile(String fileURL, String output) {
        try {
            URLConnection conn = new URL(fileURL).openConnection();
            InputStream is = conn.getInputStream();
            OutputStream outstream = new FileOutputStream(new File(output));

            byte[] buffer = new byte[4096];
            int len;

            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }

            outstream.close();
            is.close();
            System.out.println("Файл " + output + " успешно загружен.");
        } catch (IOException e) {
            System.err.println("Ошибка при скачивании " + output + ": " + e.getMessage());
        }
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
