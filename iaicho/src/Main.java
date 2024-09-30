class ChickenOrEgg extends Thread {
     String mess;  // Сообщение для вывода
  static String lastMess = ""; // Хранит последнее сообщение

    public ChickenOrEgg(String mess) {
        this.mess = mess;
    }

    @Override
    public void run() {
        int maxCount = (int)(Math.random() * 10) + 1; // Случайное количество выводов от 1 до 10
        for (int i = 0; i < maxCount; i++) { // Каждое сообщение выводится случайное количество раз
            try {
                Thread.sleep(500); // Фиксированная задержка на 500 мс
                lastMess = mess; // Сохраняем последнее сообщение
                System.out.println(mess);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Метод для получения последнего выведенного сообщения
    public static String getLastMess() {
        return lastMess; // Возвращаем последнее сообщение
    }
}

class ChickenAndEgg {
    public static void main(String[] args) {
        ChickenOrEgg egg = new ChickenOrEgg("Яйцо");
        ChickenOrEgg chicken = new ChickenOrEgg("Курица");

        egg.start(); // Запуск потока для яйца
        chicken.start(); // Запуск потока для курицы

        // Ожидание завершения потоков
        try {
            egg.join(); // Ждем завершения потока яйца
            chicken.join(); // Ждем завершения потока курицы
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, какой поток вывел последнее сообщение
        String lastMess = ChickenOrEgg.getLastMess();
        if (lastMess.equals("Яйцо")) {
            System.out.println("Победило: Яйцо!"); // Если яйцо было последним
        } else if (lastMess.equals("Курица")) {
            System.out.println("Победило: Курица!"); // Если курица была последней
        }

        // Проверка, являются ли потоки все еще активными
        if (!egg.isAlive() && !chicken.isAlive()) {
            System.out.println("Оба потока завершили свою работу.");
        }
    }
}
