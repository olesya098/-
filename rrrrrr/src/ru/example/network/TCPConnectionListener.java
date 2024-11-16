package ru.example.network;

import java.io.IOException;

// Интерфейс для обработки событий соединения TCP
public interface TCPConnectionListener {

    // Метод вызывается, когда соединение готово к использованию
    void onConnectionReady(TCPConnection tcpConnection);

    // Метод вызывается, когда получена строка от удаленного соединения
    void onReceiveString(TCPConnection tcpConnection, String s);

    // Метод вызывается, когда соединение отключается
    void onDisconnect(TCPConnection tcpConnection);

    // Метод вызывается, когда происходит исключение в соединении
    void onException(TCPConnection tcpConnection, IOException e);
}
