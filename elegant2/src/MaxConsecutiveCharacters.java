import java.nio.file.*;

class Main {
    public static void main(String[] args) throws Exception {
        // Чтение файла input.txt и сохранение в text
        String text = Files.readString(Path.of("src//input.txt"));

        //максимальная длина и текущая длина
        int maxLen = 0, currentLen = 0;
        //предыдущий символ
        char prev = 0;

        // Проход по каждому символу в строке text
        for (char c : text.toCharArray()) {
            // Пропускаем символы, которые не входят в (QRW124)
            if ("QRW124".indexOf(c) == -1) {
                currentLen = 0; // Сбрасываем текущую длину
                prev = 0; // Сбрасываем предыдущий символ
                continue; // Переходим к следующему символу
            }

            //является ли текущий символ цифрой
            boolean isCurrentDigit = Character.isDigit(c);
            //является ли предыдущий символ цифрой
            boolean isPrevDigit = Character.isDigit(prev);

            // Если это первый символ в допустимой последовательности
            if (prev == 0) {
                currentLen = 1; //  длина текущей последовательности в 1
            }
            // Если текущий символ и предыдущий символ различаются по типу
            else if (isCurrentDigit != isPrevDigit) {
                currentLen++; // Увеличиваем текущую длину
            }
            // Если текущий символ и предыдущий символ одного типа
            else {
                currentLen = 1; // Сбрасываем текущую длину на 1
            }

            // Обновляем максимальную длину, если текущая длина больше
            maxLen = Math.max(maxLen, currentLen);
            // Запоминаем текущий символ как предыдущий
            prev = c;
        }

        // Выводим максимальную длину последовательности допустимых символов
        System.out.print(maxLen);
    }
}
