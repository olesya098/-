import java.io.*;
import java.util.*;

public class Concert {

    public static void main(String[] args) {
        String inputFileName = "src/input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            // Чтение количества занятых мест
            int N = Integer.parseInt(br.readLine());
            // Map для хранения занятых мест в каждом ряду
            Map<Integer, List<Integer>> sMap = new HashMap<>();

            // Чтение занятых мест
            for (int i = 0; i < N; i++) {
                // Читаем строку, содержащую номер ряда и номер места
                String[] line = br.readLine().split(" ");
                int row = Integer.parseInt(line[0]);//преобразование в целые числа
                int seat = Integer.parseInt(line[1]);

                // Добавляем занятое место в соответствующий ряд
                sMap.putIfAbsent(row, new ArrayList<>());
                sMap.get(row).add(seat);
            }

            // Переменные для хранения результата
            int maxRow = -1; // Максимальный ряд для условий
            int minMesto = Integer.MAX_VALUE; // Минимальное место в ряду

            // Проверка каждого ряда из сидений
            for (Map.Entry<Integer, List<Integer>> e : sMap.entrySet()) {
                int row = e.getKey(); // Номер текущего ряда
                List<Integer> zanatieMesta = e.getValue(); // Занятые места в текущем ряду

                // Сортируем занятые места в текущем ряду
                bubbleSort(zanatieMesta);

                // Проверка на наличие соседних мест (например, 12 и 13)
                for (int j = 1; j < zanatieMesta.size(); j++) {
                    int tecusheeMesto = zanatieMesta.get(j); // Текущее место
                    int predidysheeMesto = zanatieMesta.get(j - 1); // Предыдущее место

                    // Проверяем, является ли текущее место соседним к предыдущему
                    if (tecusheeMesto == predidysheeMesto + 1) {
                        // Проверка, заняты ли места слева и справа
                        if (zanatieMesta.contains(tecusheeMesto - 1) &&
                                zanatieMesta.contains(tecusheeMesto + 1)) {
                            // Проверяем, обрабатываемый ряд больше, чем текущий максимум,
                            // или если это тот же ряд, но место меньше
                            if (row > maxRow || (row == maxRow && predidysheeMesto < minMesto)) {
                                maxRow = row; // Обновляем максимальный ряд
                                minMesto = predidysheeMesto; // Сохраняем номер места
                            }
                        }
                    }
                }
            }

            // Печатаем результат
            if (maxRow != -1) {
                // Если нашли удовлетворяющее условие, выводим номер ряда и место
                System.out.println(maxRow + " " + minMesto);
            } else {
                // Если не нашли подходящих мест, выводим сообщение
                System.out.println("No suitable seats found.");
            }
        } catch (IOException e) {
            // Обработка исключений ввода-вывода
            e.printStackTrace();
        }
    }

    // Метод сортировки пузырьком
    private static void bubbleSort(List<Integer> list) {
        int n = list.size();
        boolean s;

        // Проходим по всему списку
        for (int i = 0; i < n - 1; i++) {
            s = false;
            // Сравниваем соседние элементы
            for (int j = 0; j < n - 1 - i; j++) {
                // Если элементы не в порядке, меняем их местами
                if (list.get(j) > list.get(j + 1)) {
                    Collections.swap(list, j, j + 1); // Меняем местами
                    s = true; // Указываем, что произошло обмен
                }
            }
            // Если не было обменов, список уже отсортирован
            if (!s) {
                break;
            }
        }
    }
}
