import java.nio.file.*;
import java.util.regex.*;
public class main2 {

        public static void main(String[] args) throws Exception {
            String text = Files.readString(Path.of("src//input.txt"));
            Pattern p = Pattern.compile("(?:([QRW][124])+[QRW]?|([124][QRW])+[124]?)");
            Matcher m = p.matcher(text);
            int max = 0;
            while (m.find()) max = Math.max(max, m.group().length());
            System.out.println(max);
        }
    }
