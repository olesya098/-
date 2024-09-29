public class Chocolat {
    public int calculatMax(int money, int price, int wrap) {
        // количество шоколадок, которые можно купить на имеющиеся деньги
        int chocol = money / price;
        int wrappers = chocol;

        // Пока у нас есть достаточно оберток для обмена
        while (wrappers >= wrap) {
            // новые шоколадки за обертки
            int newChocolates = wrappers / wrap;
            chocol += newChocolates;

            // Обновляем количество оберток
            wrappers = newChocolates + (wrappers % wrap);
        }

        return chocol;
    }
}
