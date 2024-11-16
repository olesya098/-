package ru.example.chat.client;

import ru.example.network.TCPConnection;
import ru.example.network.TCPConnectionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    // Массив запрещенных слов для фильтрации сообщений
    String[] badWords = new String[]{
            "кошмарище",
            "ппц",
            "пошел в баню",
            "чорт"
    };

    private static final String IP_ADDR = "192.168.134.23";
    private static final int PORT = 8186;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientWindow::new);
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldInput = new JTextField();
    private JTextField fieldNickname;
    private final JButton clearChatButton = new JButton("Очистить");
    private final JComboBox<String> colorChooser = new JComboBox<>(new String[]{"Синий", "Зеленый", "Красный", "Желтый", "Белый", "Черный", "Фиолетовый", "Начальная тема"});
    private final JButton suggestButton = new JButton("Подсказка");
    private final JButton applyNicknameButton = new JButton("Сменить ник");

    private final Queue<String> messageHistory = new LinkedList<>();
    private final FrequentWordsHelper frequentWordsHelper;
    private TCPConnection connection;

    private ClientWindow() {
        frequentWordsHelper = new FrequentWordsHelper();

        setTitle("Чат с БабойЛеся");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        setupNicknamePanel();
        setupLogArea();
        setupBottomPanel();

        setVisible(true);

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    private void setupNicknamePanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Настройка поля никнейма
        fieldNickname = new JTextField("БабаЛеся", 10);
        fieldNickname.setFont(new Font("Roboto", Font.BOLD, 14));
        fieldNickname.setHorizontalAlignment(JTextField.CENTER);
        fieldNickname.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 165, 0), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Настройка кнопки смены никнейма
        applyNicknameButton.setFont(new Font("Roboto", Font.BOLD, 14));
        applyNicknameButton.setBackground(new Color(255, 165, 0));
        applyNicknameButton.setForeground(Color.WHITE);
        applyNicknameButton.setFocusPainted(false);
        applyNicknameButton.addActionListener(e -> {
            String newNickname = fieldNickname.getText().trim();
            if (!newNickname.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Никнейм успешно изменен на: " + newNickname,
                        "Смена никнейма",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Настройка выбора цвета
        colorChooser.setFont(new Font("Roboto", Font.PLAIN, 14));
        colorChooser.addActionListener(e -> changeWindowColor());

        // Добавление компонентов на верхнюю панель
        topPanel.add(new JLabel("Ник:"));
        topPanel.add(fieldNickname);
        topPanel.add(applyNicknameButton);
        topPanel.add(new JLabel("Цвет:"));
        topPanel.add(colorChooser);

        add(topPanel, BorderLayout.NORTH);
    }

    private void setupLogArea() {
        log.setEditable(false);
        log.setLineWrap(true);
        log.setFont(new Font("Monospaced", Font.PLAIN, 16));
        log.setBackground(new Color(50, 50, 50));
        log.setForeground(Color.WHITE);
        log.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 165, 0), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        add(new JScrollPane(log), BorderLayout.CENTER);
    }

    private void setupBottomPanel() {
        // Создаем основную нижнюю панель
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Панель для поля ввода
        JPanel inputPanel = new JPanel(new BorderLayout());
        fieldInput.setFont(new Font("Roboto", Font.PLAIN, 16));
        fieldInput.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 165, 0), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        fieldInput.addActionListener(this);
        inputPanel.add(fieldInput, BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Настройка кнопки очистки
        clearChatButton.setFont(new Font("Roboto", Font.BOLD, 14));
        clearChatButton.setBackground(new Color(255, 165, 0));
        clearChatButton.setForeground(Color.WHITE);
        clearChatButton.setFocusPainted(false);
        clearChatButton.addActionListener(e -> {
            clearAllData();
            JOptionPane.showMessageDialog(this,
                    "Чат и история слов были успешно очищены.",
                    "Очистка данных",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Настройка кнопки подсказки
        suggestButton.setFont(new Font("Roboto", Font.BOLD, 14));
        suggestButton.setBackground(new Color(255, 165, 0));
        suggestButton.setForeground(Color.WHITE);
        suggestButton.setFocusPainted(false);
        suggestButton.addActionListener(e -> showFrequentWords());

        // Добавление кнопок на панель
        buttonPanel.add(clearChatButton);
        buttonPanel.add(suggestButton);

        // Добавление панелей в основную нижнюю панель
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Добавление нижней панели в окно
        add(bottomPanel, BorderLayout.SOUTH);
    }



    private void setupClearButton() {
        clearChatButton.setFont(new Font("Roboto", Font.BOLD, 16)); // Установка шрифта для кнопки
        clearChatButton.setBackground(new Color(255, 165, 0)); // Установка цвета фона кнопки (оранжевый)
        clearChatButton.setForeground(Color.WHITE); // Установка цвета текста
        clearChatButton.setFocusPainted(false); // Убираем эффект фокуса

        // Установка предпочтительного размера кнопки
        clearChatButton.setPreferredSize(new Dimension(150, 40)); // Ширина 150, высота 40 пикселей

        // Добавление обработчика события нажатия на кнопку
        clearChatButton.addActionListener(e -> {
            clearAllData(); // Вызов метода для очистки данных
            // Показ сообщения об успешной очистке данных
            JOptionPane.showMessageDialog(this, "Чат и история слов были успешно очищены.",
                    "Очистка данных", JOptionPane.INFORMATION_MESSAGE);
        });
    }



    // Метод для настройки выбора цвета
    private void setupColorChooser() {
        colorChooser.setFont(new Font("Roboto", Font.PLAIN, 16));
        // для изменения цвета окна
        colorChooser.addActionListener(e -> changeWindowColor());
    }


    // Метод для создания панели выбора цвета
    private JPanel createColorPanel() {
        JPanel colorPanel = new JPanel(); // Создание новой панели
        colorPanel.add(new JLabel("Цвет:")); // Добавление метки "Цвет:"
        colorPanel.add(colorChooser); // Добавление выпадающего списка выбора цвета
        return colorPanel; // Возврат созданной панели
    }

    // Метод для настройки кнопки подсказки
    private void setupSuggestButton() {
        // шрифт для кнопки подсказки
        suggestButton.setFont(new Font("Roboto", Font.BOLD, 16));
        // цвет фона кнопки
        suggestButton.setBackground(new Color(255, 165, 0)); // Оранжевый цвет
        // цвет текста кнопки
        suggestButton.setForeground(Color.WHITE);

        //обработчик событий
        suggestButton.addActionListener(e -> showFrequentWords());
    }

    // Метод для очистки
    private void clearAllData() {
        // Очищаем область логов чата
        log.setText("");
        // Очищаем историю сообщений
        messageHistory.clear();

        // Очищаем историю слов в помощнике
        frequentWordsHelper.clearAllData();
    }

    // Метод для изменения цвета окна на основе выбранного цвета
    private void changeWindowColor() {
        // Получение текущего выбранного цвета
        String selectedColor = (String) colorChooser.getSelectedItem();
        // Преобразование его в объект Color
        Color windowColor = getColorFromString(selectedColor);

        // Установка фона главного контейнера окна
        getContentPane().setBackground(windowColor);
        // Задаем более темный фон для различных компонентов
        log.setBackground(windowColor.darker());
        fieldInput.setBackground(windowColor.darker());
        fieldNickname.setBackground(windowColor.darker());
        clearChatButton.setBackground(windowColor.darker());
        colorChooser.setBackground(windowColor.darker());
        suggestButton.setBackground(windowColor.darker());
        // Перерисовываем окно для применения изменений
        revalidate();
        repaint();
    }

    // Метод для получения объекта Color на основе названия цвета
    private Color getColorFromString(String colorName) {
        // Используем switch для сопоставления названия цвета с объектом Color
        return switch (colorName) {
            case "Синий" -> Color.BLUE;
            case "Зеленый" -> Color.GREEN;
            case "Красный" -> Color.RED;
            case "Желтый" -> Color.YELLOW;
            case "Белый" -> Color.WHITE;
            case "Черный" -> Color.BLACK;
            case "Фиолетовый" -> new Color(128, 0, 128); // RGB для фиолетового
            case "Начальная тема" -> new Color(50, 50, 50); // Более темный фон
            default -> Color.GRAY; // Стандартный цвет, если введено некорректное значение
        };
    }

    // Обработка события нажатия на кнопку отправки сообщения
    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText().trim(); // Получение текста сообщения
        if (msg.isEmpty()) return; // Завершение метода, если сообщение пустое
        fieldInput.setText(""); // Очистка поля ввода после отправки

        // Проверка, установлено ли TCP-соединение
        if (connection != null) {
            String nickname = fieldNickname.getText().trim(); // Получение никнейма
            String formattedMessage = nickname + ": " + msg; // Форматирование сообщения
            connection.sendString(formattedMessage); // Отправка сообщения через соединение
            updateMessageHistory(formattedMessage); // Обновление истории сообщений

            frequentWordsHelper.trackWordUsage(msg); // Отслеживание использования слов
        } else {
            printMessage("Cannot send message. Connection is not established."); // Сообщение об ошибке
        }
    }

    // Метод для обновления истории сообщений (хранит последние 3 сообщения)
    private void updateMessageHistory(String message) {
        messageHistory.offer(message); // Добавление нового сообщения в историю
        if (messageHistory.size() > 3) {
            messageHistory.poll(); // Удаление старого сообщения, если больше 3
        }
    }

    // Методы для различных событий TCP-соединения
    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready..."); // Сообщение о том, что соединение готово
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value); // Вывод принятого сообщения
        updateMessageHistory(value); // Обновление истории сообщений
        frequentWordsHelper.trackWordUsage(value); // Отслеживание использования слов
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection closed."); // Сообщение о закрытии соединения
    }

    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
        printMessage("Connection exception: " + e); // Сообщение об ошибке соединения
    }

    // Синхронизированный метод для вывода сообщений в лог
    private synchronized void printMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            // Разделяем сообщение на части
            String[] parts = message.split(": ");
            if (parts.length > 1) {
                String mes = parts[1]; // Получаем текст сообщения
                boolean isBadWord = false; // Переменная для отслеживания наличия запрещенного слова
                // Проверка на наличие запрещенных слов
                for (String word : badWords) {
                    if (mes.toLowerCase().equals(word.toLowerCase())) {
                        log.append(message.replace(mes, "***") + "\n"); // Замена запрещенного слова на ***
                        isBadWord = true; // Устанавливаем флаг о наличии плохого слова
                        break;
                    }
                }
                // Если запрещенных слов нет, выводим сообщение как есть
                if (!isBadWord) {
                    log.append(message + "\n");
                }
            } else {
                log.append(message + "\n"); // Выводим сообщение, если оно не соответствует формату
            }
        });
    }

    // Метод для отображения популярных слов
    private void showFrequentWords() {
        // Получаем массив из 5 самых популярных слов из помощника
        String[] topWords = frequentWordsHelper.getTopWords(5);

        // Проверка, есть ли популярные слова
        if (topWords.length > 0) {
            // Формируем строку с популярными словами
            String suggestion = "Популярные слова: " + String.join(", ", topWords);
            // Показ сообщения с популярными словами в диалоговом окне
            JOptionPane.showMessageDialog(this, suggestion, "Подсказки",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Если нет популярных слов, отображаем соответствующее сообщение
            JOptionPane.showMessageDialog(this, "Нет часто используемых слов",
                    "Подсказки", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class FrequentWordsHelper {
        // Константа для имени файла, в который будут сохранены частотные слова
        private static final String WORDS_FILE = "frequent_words.txt";

        // Хранит частоту использования каждого слова
        private Map<String, Integer> wordFrequency = new HashMap<>();

        // Приоритетная очередь для хранения слов в порядке убывания их частоты
        private PriorityQueue<Map.Entry<String, Integer>> topWords =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        // Флаг для определения, является ли это первым сообщением
        private boolean isFirstMessage = true;

        // Конструктор: загружает слова из файла при создании объекта
        public FrequentWordsHelper() {
            loadWordsFromFile();
        }

        // Очищает все данные, включая очистку частот и удаление файла
        public void clearAllData() {
            wordFrequency.clear(); // Очищаем частоту слов
            topWords.clear(); // Очищаем список популярных слов
            isFirstMessage = true; // Сбрасываем флаг первого сообщения

            try {
                File file = new File(WORDS_FILE);
                // Удаляем файл, если он существует
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace(); // Обработка исключений при удалении файла
            }
        }

        // Отслеживает использование слов в сообщении
        public void trackWordUsage(String message) {
            // Пропускаем первое сообщение, чтобы избежать учета его
            if (isFirstMessage) {
                isFirstMessage = false;
                return;
            }

            // Разбиваем сообщение на никнейм и текст
            String[] messageParts = message.split(": ", 2);
            String nickname = "";
            String text = message;

            // Проверяем, есть ли никнейм в сообщении
            if (messageParts.length > 1) {
                nickname = messageParts[0].toLowerCase(); // Сохраняем никнейм в нижнем регистре
                text = messageParts[1].toLowerCase(); // Сохраняем текст в нижнем регистре
            }

            // Разбиваем текст на слова
            String[] words = text.split("\\s+");
            for (String word : words) {
                // Пропускаем короткие слова, числа и никнеймы
                if (word.length() < 2
                        || word.matches("\\d+")
                        || word.equalsIgnoreCase(nickname) ||
                        word.equalsIgnoreCase("бабалеся")) {
                    continue;
                }
                // Обновляем частоту слова
                wordFrequency.merge(word, 1, Integer::sum);
                updateTopWords(); // Обновляем список популярных слов
            }
            // Сохраняем обновленные слова в файл
            saveWordsToFile();
        }

        // Загружает слова и их частоты из файла
        private void loadWordsFromFile() {
            File file = new File(WORDS_FILE);
            // Если файл не существует, выходим из метода
            if (!file.exists()) {
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                // Читаем файл построчно
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    // Если строка содержит корректные данные, добавляем их в карту частот
                    if (parts.length == 2) {
                        wordFrequency.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
                updateTopWords(); // Обновляем список популярных слов
                // Сбрасываем флаг первого сообщения при загрузке из файла
                isFirstMessage = false;

            } catch (IOException e) {
                e.printStackTrace(); // Обработка исключений при чтении файла
            }
        }

        // Сохраняет слова и их частоты в файл
        private void saveWordsToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(WORDS_FILE))) {
                // Записываем каждую пару слово-частота в файл
                for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Обработка исключений при записи в файл
            }
        }

        // Обновляет список популярных слов на основе текущей частоты
        private void updateTopWords() {
            topWords.clear(); // Очищаем предыдущие данные о популярных словах
            // Добавляем слова с частотой 4 и выше
            wordFrequency.entrySet().stream()
                    .filter(entry -> entry.getValue() >= 4)
                    .forEach(topWords::add);
        }

        // Возвращает массив популярных слов, учитывая заданное количество
        public String[] getTopWords(int count) {
            List<String> result = new ArrayList<>(); // Список для строк результата
            PriorityQueue<Map.Entry<String, Integer>> tempQueue = new PriorityQueue<>(topWords); // Копия приоритетной очереди

            // Извлекаем слова из очереди до тех пор, пока не достигнем желаемого количества
            while (result.size() < count && !tempQueue.isEmpty()) {
                Map.Entry<String, Integer> entry = tempQueue.poll();
                if (entry.getValue() >= 4) { // Условие для слов
                    result.add(entry.getKey() + " (" + entry.getValue() + " раз)"); // Форматируем результат
                }
            }

            // Возвращаем результат в виде массива строк
            return result.toArray(new String[0]);
        }
    }
}

