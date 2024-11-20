package ru.demo.socket.inc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Server {
    // Множество для хранения всех доступных городов
    private static Set<String> cities;
    // Множество для хранения уже использованных городов в текущей игре
    private static final Set<String> usedCities = new HashSet<>();
    // Путь к файлу со списком городов
    private static final String CITIES_FILE = "src//cities.txt";

    // Статический блок инициализации, выполняется при загрузке класса
    static {
        try {
            loadCitiesFromFile();
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке городов из файла: " + e.getMessage());
            // Создаем базовый список городов при ошибке загрузки файла
            cities = new HashSet<>(Arrays.asList(
                    "Москва", "Архангельск", "Киев", "Владимир", "Рязань"
            ));
        }
    }

    // Метод загрузки городов из файла
    private static void loadCitiesFromFile() throws IOException {
        // Проверяем существование файла
        if (!Files.exists(Paths.get(CITIES_FILE))) {
            // Создаем файл с начальным списком городов, если он отсутствует
            List<String> initialCities = Arrays.asList(
                    "Москва", "Архангельск", "Киев", "Владимир", "Рязань", "Новгород",
                    "Димитровград", "Дмитров", "Воронеж", "Жуковский", "Йошкар-Ола",
                    "Астрахань", "Новосибирск", "Краснодар", "Ростов", "Волгоград",
                    "Донецк", "Казань", "Набережные Челны", "Ярославль", "Тамбов"
            );
            Files.write(Paths.get(CITIES_FILE), initialCities, StandardCharsets.UTF_8);
        }

        // Загружаем города из файла
        cities = new HashSet<>(Files.readAllLines(Paths.get(CITIES_FILE), StandardCharsets.UTF_8));

        // Обрабатываем список городов: удаляем пробелы, пустые строки и форматируем название
        cities = cities.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Server::capitalize)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);

        System.out.println("Загружено городов из файла: " + cities.size());
    }

    // Основной метод сервера
    public static void main(String[] args) throws IOException {
        // Создаем серверный сокет на порту 8080
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Сервер запущен и ждет подключения...");

        // Бесконечный цикл обработки подключений
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Новое подключение: " + clientSocket.getInetAddress().toString());

                String clientCity;
                // Обрабатываем ходы клиента
                while ((clientCity = reader.readLine()) != null) {
                    clientCity = clientCity.trim().toLowerCase();
                    System.out.println("Получено от клиента: " + clientCity);

                    // Проверяем валидность города
                    if (!cities.contains(capitalize(clientCity))) {
                        writer.println("ERROR: Такого города нет в списке!");
                        continue;
                    }

                    if (usedCities.contains(clientCity)) {
                        writer.println("ERROR: Этот город уже был использован!");
                        continue;
                    }

                    // Добавляем город в использованные
                    usedCities.add(clientCity);

                    // Ищем ответный город
                    String lastChar = getLastChar(clientCity);
                    String serverCity = findCity(lastChar);

                    // Если не нашли подходящий город - клиент победил
                    if (serverCity == null) {
                        writer.println("WIN: Поздравляем! Я не могу найти подходящий город. Вы выиграли!");
                        usedCities.clear();
                        break;
                    }

                    usedCities.add(serverCity.toLowerCase());
                    writer.println(serverCity);
                }
            } catch (IOException e) {
                System.out.println("Ошибка при обработке клиента: " + e.getMessage());
            }
        }
    }

    // Метод поиска подходящего города
    private static String findCity(String startChar) {
        return cities.stream()//поток
                .filter(city -> city.toLowerCase().startsWith(startChar)//Проверяет, начинается ли название города (в нижнем регистре) с заданного символа startChar
                        && !usedCities.contains(city.toLowerCase()))//Проверяет, не содержится ли город в множестве уже использованных городов
                .findFirst()
                .orElse(null);
    }

    // Метод получения последней буквы города с учетом специальных случаев
    private static String getLastChar(String city) {
        String lastChar = city.substring(city.length() - 1).toLowerCase();
        // Если последняя буква - ь, ъ, ы или й, берем предпоследнюю букву
        if (lastChar.equals("ь") || lastChar.equals("ъ") || lastChar.equals("ы") || lastChar.equals("й")) {
            return city.substring(city.length() - 2, city.length() - 1);
        }
        return lastChar;
    }

    // Метод форматирования названия города (первая буква заглавная, остальные строчные)
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}