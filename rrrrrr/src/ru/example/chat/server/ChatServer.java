package ru.example.chat.server;

import ru.example.network.TCPConnection;
import ru.example.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

@SuppressWarnings("InfiniteLoopStatement")
public class ChatServer implements TCPConnectionListener {
    // Точка входа в программу, где создается экземпляр ChatServer
    public static void main(String[] args) {
        new ChatServer(); // Запуск сервера
    }

    // Список активных TCP-соединений
    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    // Конструктор сервера
    private ChatServer() {
        System.out.println("Server running..."); // Сообщение о запуске сервера
        try (ServerSocket serverSocket = new ServerSocket(8186)) { // Создание серверного сокета на порту 8186
            while (true) {
                try {
                    // Ожидание принятия нового соединения; создание нового TCPConnection для каждого клиента
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e); // Обработка ошибок подключения
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // Обработка ошибок при создании серверного сокета
        }
    }

    // Обработка события готовности соединения (новый клиент подключен)
    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection); // Добавление нового соединения в список активных соединений
        sendToAllConnections("Client connected: " + tcpConnection); // Уведомление всех клиентов о новом подключении
    }

    // Обработка полученной строки от клиента
    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value); // Пересылка полученного сообщения всем подключенным клиентам
    }

    // Обработка события отключения клиента
    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection); // Удаление отключенного соединения из списка
        sendToAllConnections("Client disconnected: " + tcpConnection); // Уведомление всех клиентов об отключении
    }

    // Обработка исключения, связанного с соединением
    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
        System.out.println("TCPConnection exception: " + e); // Вывод информации об ошибке в консоль
    }

    // Метод для отправки сообщения всем подключенным клиентам
    private void sendToAllConnections(String value) {
        System.out.println(value); // Вывод сообщения на консоль
        for (TCPConnection connection : connections) {
            connection.sendString(value); // Отправка сообщения каждому подключенному клиенту
        }
    }
}
