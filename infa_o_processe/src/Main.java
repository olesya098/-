import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя процесса для запуска (например: notepad.exe, calc.exe, mspaint.exe): ");
        String processName = scanner.nextLine(); // Получаем имя процесса от пользователя

        Process process = null; // Объявляем переменную для хранения процесса

        try {
            ProcessBuilder procBuilder = new ProcessBuilder();
            procBuilder.command(processName); // команда в соответствии с вводом пользователя

            process = procBuilder.start(); // Запуск процесса

            // Формирование информации о всех текущих процессах
            System.out.println("\nТекущие процессы:");
            try {
                Process taskListProcess = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader processReader = new BufferedReader(new InputStreamReader(taskListProcess.getInputStream()));
                // Создаем BufferedReader для чтения вывода команды
                String line;// Переменная для хранения строки
// Чтение и вывод каждой строки из списка процессов
                while ((line = processReader.readLine()) != null) {
                    System.out.println(line); // Вывод текущих процессов
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Ожидание перед закрытием
            System.out.println("Процесс запущен. Введите PID процесса для его закрытия:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));//для чтения ввода пользователя
            String pidInput = reader.readLine(); // Чтение PID от пользователя

            try {
                int pid = Integer.parseInt(pidInput); // Преводим введенную строку в целое число (PID)

                String command = "taskkill /F /PID " + pid; //  команда для завершения процесса
                Process killProcess = Runtime.getRuntime().exec(command);
                killProcess.waitFor(); // Ожидаем завершения команды
                System.out.println("Процесс с PID " + pid + " был закрыт.");

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введен неверный PID.");
            } catch (IOException | InterruptedException e) {
                System.out.println("Ошибка при попытке закрыть процесс: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок ввода-вывода
        } finally {
            // Закрытие процесса, если он все еще запущен
            if (process != null) {
                process.destroy(); // Убиваем процесс, если он все еще существует
            }
            scanner.close(); // Закрытие сканера
        }
    }
}
