
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeMinimiz {

    public static void main(String[] args) {
        // Путь к входному файлу
        String inputFilePath = "G://Системное программирование//file1.java";
        // Путь к выходному файлу
        String outputFilePath = "G://Системное программирование//file2.java";

        try {
            // Чтение содержимого файла и преобразование его в строку
            String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));

            // удаление комментариев, сокращение имен
            String compressedContent = compressCode(content);

            // Запись сжатого кода в выходной файл
            Files.write(Paths.get(outputFilePath), compressedContent.getBytes());
            System.out.println("Файл " + outputFilePath + " успешно создан.");
        } catch (IOException e) {
            // Обработка исключений при чтении или записи файла
            e.printStackTrace();
        }
    }

    private static String compressCode(String code) {
        // Удаление многострочных комментариев
        code = code.replaceAll("/\\*[^*]*(?:\\*(?!/)[^*]*)*\\*/", "");

        // Компилируем регулярное выражение для однострочных комментариев
        Pattern singleLineCommentPattern = Pattern.compile("//.*");
        Matcher matcher = singleLineCommentPattern.matcher(code);
        // Удаление однострочных комментариев
        code = matcher.replaceAll("");

        // Находим все идентификаторы в коде (имена переменных, методов и т.д.)
        Set<String> identifiers = findIdentifiers(code);

        // Создаем мап идентификаторов на короткие имена
        Map<String, String> identifierMapping = createIdentifierMapping(identifiers);

        // Заменяем идентификаторы на короткие имена в коде
        for (Map.Entry<String, String> entry : identifierMapping.entrySet()) {
            code = code.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue());
        }

        // Удаление лишних пробелов и переводов строк для упрощения
        code = code.replaceAll("\\s+", " ");

        // Удаление пробелов перед и после фигурных скобок, операторов и т.д.
        code = code.replaceAll("\\s*([{}();=+\\-*/<>])\\s*", "$1");

        return code.trim(); // Возвращение кода
    }

    private static Set<String> findIdentifiers(String code) {
        Set<String> identifiers = new HashSet<>(); // Множество для хранения уникальных идентификаторов
        // Регулярное выражение для поиска идентификаторов (буквы и цифры)
        Pattern identifierPattern = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
        Matcher matcher = identifierPattern.matcher(code);

        // Поиск всех идентификаторов в коде
        while (matcher.find()) {
            String identifier = matcher.group();
            // Проверка, является ли найденный идентификатор зарезервированным словом
            if (!isReservedWord(identifier)) {
                identifiers.add(identifier); // Добавление идентификатора в множество
            }
        }

        return identifiers; // Возвращение найденных идентификаторов
    }

    private static Map<String, String> createIdentifierMapping(Set<String> identifiers) {
        Map<String, String> mapping = new HashMap<>(); // Создание отображения для идентификаторов
        String[] shortNames = generateShortNames(identifiers.size()); // Генерация коротких имен

        int index = 0;

        // Создание мап от идентификаторов к коротким именам
        for (String identifier : identifiers) {
            mapping.put(identifier, shortNames[index++]);
        }

        return mapping; // Возвращение созданного мап
    }

    private static String[] generateShortNames(int count) {
        String[] names = new String[count]; // Массив для хранения коротких имен
        int index = 0;

        // Генерация однобуквенных
        for (char c = 'a'; c <= 'z' && index < count; c++) {
            names[index++] = String.valueOf(c);
        }

        // Генерация двухбуквенных имен, если однобуквенных не хватило
        for (char c = 'a'; c <= 'z' && index < count; c++) {
            for (char d = 'a'; d <= 'z' && index < count; d++) {
                names[index++] = String.valueOf(c) + d; // Создание комбинации
            }
        }

        return names; // Возвращение массива имен
    }

    private static boolean isReservedWord(String word) {
        // Создание множества, содержащего все зарезервированные слова Java.
        Set<String> reservedWords = Set.of(
                "abstract", "assert", "boolean", "break", "byte", "case", "catch",
                "char", "class", "const", "continue", "default", "do", "double",
                "else", "enum", "extends", "final", "finally", "float", "for",
                "goto", "if", "implements", "import", "instanceof", "int",
                "interface", "long", "native", "new", "package", "private",
                "protected", "public", "return", "short", "static", "strictfp",
                "super", "switch", "synchronized", "this", "throw", "throws",
                "transient", "try", "void", "volatile", "while"
        );

        // Возвращает true, если переданное слово содержится в множестве
        return reservedWords.contains(word);
    }
}
