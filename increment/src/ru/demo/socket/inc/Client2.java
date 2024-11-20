package ru.demo.socket.inc;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Клиент 2 подключен к серверу. Игра началась!");
            System.out.println("Введите название города (или 'выход' для завершения):");

            while (true) {
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("выход")) {
                    break;
                }

                // Просто отправляем введенный текст серверу
                writer.println(input);

                // Получаем и отображаем ответ
                String response = reader.readLine();
                System.out.println("Ответ от сервера: " + response);

                // Проверяем только на завершение игры
                if (response.startsWith("WIN:")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка подключения к серверу: " + e.getMessage());
        }
    }
}

