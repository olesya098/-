// Класс AnimalThread, который расширяет Thread
class AnimalThread extends Thread {
    private final String name;
    private int prioritet;
    // Дистанция, которую животное должно пробежать
    private static final int D = 100;

    public AnimalThread(String name, int priority) {
        this.name = name;
        this.prioritet = priority;
        setName(name); // Установка имени потока
        setPriority(priority); // Установка приоритета потока
    }

    @Override
    public void run() {
        int distance = 0;

        for (; distance < D; ) { // Инициализация без условия и увеличение внутри цикла
            distance += (int) (Math.random() * 10) + 1; // Бегут случайное количество метров от 1 до 10 (включительно)
            System.out.println(name + " пробежал: " + distance + " метров.");

            // смена приоритета
            if (distance < 33) {
                setPriority(Thread.MAX_PRIORITY);
            } else if (distance >= 33 && distance < 100) {
                setPriority(Thread.MIN_PRIORITY);
            }
        }
        System.out.println(name + " победил");
    }
}


    // Главный класс для создания и запуска потоков
 class RabbitAndTurtle {
    public static void main(String[] args) {
        AnimalThread rabbit = new AnimalThread("Кроллер", Thread.NORM_PRIORITY);
        AnimalThread turtle = new AnimalThread("Черепах", Thread.NORM_PRIORITY);
// Запуск потоков для кролика и черепахи
        rabbit.start();
        turtle.start();
    }
}
