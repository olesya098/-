package ru.demo.socket.inc;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // Подключаемся к серверу и создаем потоки ввода-вывода
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключено к серверу. Игра началась!");
            System.out.println("Введите название города (или 'выход' для завершения):");


            while (true) {
                String city = scanner.nextLine();

                // Проверка на выход из игры
                if (city.equalsIgnoreCase("выход")) {
                    break;
                }

                // Отправляем город серверу
                writer.println(city);
                String response = reader.readLine();

                // Обработка ответов
                if (response.startsWith("ERROR:")) {

                    System.out.println(response.substring(6));
                    continue;
                }

                if (response.startsWith("WIN:")) {
                    // Выводим сообщение о победе
                    System.out.println(response.substring(5));
                    break;
                }


                System.out.println("Ответ сервера: " + response);
                String nextChar = getLastChar(response).toUpperCase();
                System.out.println("Ваш ход (город должен начинаться на букву '" + nextChar + "'):");
            }

        } catch (IOException e) {
            System.out.println("Ошибка подключения к серверу: " + e.getMessage());
        }
    }

    // Метод получения последней буквы города с учетом специальных случаев
    private static String getLastChar(String city) {
        city = city.toLowerCase();

        // Обработка окончания "ый"
        if (city.endsWith("ый")) {
            return city.substring(city.length() - 3, city.length() - 2);
        }

        // Обработка окончания "ий"
        if (city.endsWith("ий")) {
            return city.substring(city.length() - 3, city.length() - 2);
        }

        // Обработка специальных окончаний (ь, ъ, ы, й)
        String lastChar = city.substring(city.length() - 1);
        if (lastChar.equals("ь") || lastChar.equals("ъ") ||
                lastChar.equals("ы") || lastChar.equals("й")) {
            return city.substring(city.length() - 2, city.length() - 1);
        }

        return lastChar;
    }
}