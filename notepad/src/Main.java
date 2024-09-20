import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
int i = 3;
        while (i>0){//3 раза создаем и убиваем процесс
            try{

                ProcessBuilder procBuilder = new ProcessBuilder();//Создаем новый объект ProcessBuilder
                procBuilder.command("mspaint.exe");//команда для запуска paint
                //тоже можно сделать для блокнота, заменив mspaint на notepad
                Process process = procBuilder.start(); // запуск программы
                try {
                    Thread.sleep(800);//Приостанавливаем выполнение программы на 1 секунду
                } catch (InterruptedException ex) {
                }
                process.destroy(); //убийство
            }
            catch (IOException e)
            {
                e.printStackTrace();// Выводим стек вызовов ошибки
            }

i--;
        }
    }
}
