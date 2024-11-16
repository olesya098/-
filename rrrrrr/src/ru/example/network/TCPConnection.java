package ru.example.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * РљР»Р°СЃСЃ РїСЂРµРґСЃС‚Р°РІР»СЏРµС‚ TCP-СЃРѕРµРґРёРЅРµРЅРёРµ
 */
public class TCPConnection {
    private final Socket socket; // Сокет для установленного TCP соединения
    private final Thread thread; // Поток для обработки входящих сообщений
    private final TCPConnectionListener eventListener; // Слушатель для обработки событий соединения
    private final BufferedReader in; // Для чтения данных из сокета
    private final BufferedWriter out; // Для записи данных в сокет

    // Конструктор, принимающий слушателя, IP-адрес и порт
    public TCPConnection(TCPConnectionListener eventListener, String ipAddress, int port) throws IOException {
        this(eventListener, new Socket(ipAddress, port)); // Создание нового сокета с указанными параметрами
    }

    // Конструктор, который принимает слушателя и уже созданный сокет
    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener; // Инициализация слушателя
        this.socket = socket; // Инициализация сокета
        // Создание BufferedReader для чтения входящих данных, используя кодировку UTF-8
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        // Создание BufferedWriter для записи исходящих данных, используя кодировку UTF-8
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        // Инициализация потока для обработки входящих сообщений
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this); // Уведомление о готовности соединения
                    while (!thread.isInterrupted()) {
                        // Чтение строки из сокета и уведомление слушателя о ее получении
                        eventListener.onReceiveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e); // Уведомление о возникшей ошибке
                } finally {
                    eventListener.onDisconnect(TCPConnection.this); // Вызов метода при отключении
                }
            }
        });
        thread.start(); // Запуск потока
    }

    // Синхронизированный метод для отправки строки через соединение
    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n"); // Запись значения с переносом строки
            out.flush(); // Принудительная отправка данных
        } catch (IOException e) {
            eventListener.onException(this, e); // Уведомление об ошибке при отправке
            disconnect(); // Отключение в случае ошибки
        }
    }

    // Синхронизированный метод для отключения соединения
    private synchronized void disconnect() {
        thread.interrupt(); // Прерывание потока
        try {
            socket.close(); // Закрытие сокета
        } catch (IOException e) {
            eventListener.onException(this, e); // Уведомление об ошибке
        }
    }

    // Переопределение метода toString для вывода информации о соединении
    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort(); // Возвращение строки с адресом и портом
    }
}
