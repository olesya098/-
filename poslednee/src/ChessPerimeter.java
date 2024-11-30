import java.util.Scanner;

public class ChessPerimeter {
    // булевый массив
    private static boolean[][] array = new boolean[9][9];
    private static int w; // Переменная для хранения количества выпиленных клеток

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение количества выпиленных клеток
        w = scanner.nextInt();

        // Заполнение доски
        for (int i = 0; i < w; i++) {
            int row = scanner.nextInt(); // Читаем номер строки (от 1 до 8)
            int col = scanner.nextInt(); // Читаем номер столбца (от 1 до 8)
            // соответствующая клетка в массиве = true, чтобы отметить, что эта клетка выпилена
            array[row][col] = true;
        }

        // Подсчет периметра
        int perimeter = Perimeter(); // Вызываем метод для расчета периметра
        System.out.println(perimeter); // Выводим периметр

        scanner.close();
    }

    private static int Perimeter() {
        int perimeter = 0; // для хранения периметра

        // Проверяем каждую клетку от 1 до 8
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                // является ли текущая клетка выпиленной
                if (array[i][j]) {
                    // Если клетки сверху не существует увеличиваем периметр
                    if (!array[i - 1][j]) perimeter++;
                    // Если клетки снизу не существует увеличиваем периметр
                    if (!array[i + 1][j]) perimeter++;
                    // Если клетки слева не существует увеличиваем периметр
                    if (!array[i][j - 1]) perimeter++;
                    // Если клетки справа не существует увеличиваем периметр
                    if (!array[i][j + 1]) perimeter++;
                    //Если соседняя клетка не выпилена (false)
                }
            }
        }
        return perimeter; // Возвращаем вычисленный периметр
    }
}
