import java.io.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("fl.txt"))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            String text = content.toString();
            Pattern pattern = Pattern.compile("((?:AB|CB)+)");
            Matcher matcher = pattern.matcher(text);

            int maxLength = 0;
            while (matcher.find()) {
                int length = matcher.group(1).length() / 2;
                maxLength = Math.max(maxLength, length);
            }

            System.out.println(maxLength);

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
