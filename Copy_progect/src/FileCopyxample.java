import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
class FileCopy {

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static void main(String[] args) {
        // Укажите путь к исходному файлу и целевому файлу
        String source = "src/www.txt"; // замените на фактический путь
        String dest = "src/eee.txt"; // замените на фактический путь

        File sourceFile = new File(source);
        File destFile = new File(dest);

        try {
            copyFileUsingStream(sourceFile, destFile);
            System.out.println("Файл успешно скопирован!");
        } catch (IOException e) {
            System.err.println("Ошибка при копировании файла: " + e.getMessage());
        }
    }
}
