public class Main {
    public static void main(String[] args) {
        int money = 15;
        int price = 1;
        int wrap = 3;

        // Создаем экземпляр
        Chocolat chocolCalc = new Chocolat();

        // Вычисление максимального количества шоколадок
        int maxChocolates = chocolCalc.calculatMax(money, price, wrap);

        // Вывод результата
        System.out.println("Максимальное количество шоколадок: " + maxChocolates);
    }
}
