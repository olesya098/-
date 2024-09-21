import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите процесс для запуска:");
        System.out.println("1. Блокнот (notepad.exe)");
        System.out.println("2. Калькулятор (calc.exe)");
        System.out.println("3. Рисунок (mspaint.exe)");
        System.out.print("Введите номер процесса (1-3): ");

        int choice = scanner.nextInt();


        for (int i = 0; i < 3; i++) {
            try {
                ProcessBuilder procBuilder = new ProcessBuilder();

                // Определяем команду в зависимости от выбора пользователя
                switch (choice) {
                    case 1:
                        procBuilder.command("notepad.exe");
                        break;
                    case 2:
                        procBuilder.command("calc.exe");

                        break;
                    case 3:
                        procBuilder.command("mspaint.exe");
                        break;
                    default:
                        System.out.println("Неправильный выбор. Завершение программы.");
                        return; // Завершаем если выбор неверный
                }

                Process process = procBuilder.start(); // Запуск процесса
                try {
                    Thread.sleep(1000); // Ждем 1 секунду
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
               if (choice==2){
                   // Перезагрузка компьютера
                   Process restartProcess = Runtime.getRuntime().exec("shutdown -r -t 0");//shutdown -r -t 0 для перезагрузки компьютера
                   restartProcess.waitFor(); // убедиться, что перезагрузка действительно инициирована
               } else {
                   process.destroy(); // Убиваем процесс для других приложений
               }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        scanner.close(); // Закрытие сканера
    }
}
