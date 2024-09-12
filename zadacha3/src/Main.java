import java.util.*;

public class Main {
    public static void main(String[] args) {
        final int MAX_PAR = 16; // Максимальное количество пар
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество пришедших sms-сообщений: ");// количество сообщений
        int Votes = scanner.nextInt();

        // Создание массива для хранения голосов
        int[] votes = new int[MAX_PAR + 1]; // Индексы с 1 по 16, 0 не используется

        System.out.println("Введите номера пар от 1 до 16:");
        for (int i = 0; i < Votes; i++) {
            int pairNumber = scanner.nextInt();// Считываем номер пары
            if (pairNumber >= 1 && pairNumber <= MAX_PAR) {// Проверяем на допустимость ввода
                votes[pairNumber]++;// Увеличиваем голоса для данной пары
            } else {
                System.out.println("Некорректный номер пары: " + pairNumber);
            }
        }

        // Создание списка пар с их голосами
        List<PairVotes> pairVotesList = new ArrayList<>();
        for (int i = 1; i <= MAX_PAR; i++) {// Проходим по всем возможным парам
            if (votes[i] > 0) { // Если у пары есть голоса, добавляем ее в список
                pairVotesList.add(new PairVotes(i, votes[i]));
            }
        }

        //  пузырьковая сортировка
        bubbleSort(pairVotesList);

        // Вывод результатов
        System.out.println("Результаты голосования:");
        for (PairVotes pairVotes : pairVotesList) {
            System.out.println("Пара " + pairVotes.getPairNumber() + ": " + pairVotes.getVote() + " голосов");
        }

        // Вывод полного списка пар
        System.out.println("Полный список пар:");
        for (int i = 1; i <= MAX_PAR; i++) {
            System.out.println("Пара " + i + ": " + votes[i] + " голосов");
        }

    }

    // Класс для хранения номера пары и количества голосов
    static class PairVotes {
        private final int pairNumber;
        private final int vote;

        public PairVotes(int pairNumber, int voteCount) {
            this.pairNumber = pairNumber;
            this.vote = voteCount;
        }

        public int getPairNumber() {
            return pairNumber;
        }

        public int getVote() {
            return vote;
        }
    }

    // Метод для сортировки пузырьком
    static void bubbleSort(List<PairVotes> list) {
        int n = list.size(); // Получаем размер списка
        boolean s; // были ли обмены в текущем проходе
        do {
            s = false; //  показатель обмена
            for (int i = 0; i < n - 1; i++) { // Проходим по списку
                // Если текущий  имеет меньшее количество голосов, чем следующий, меняем их местами
                if (list.get(i).getVote() < list.get(i + 1).getVote()) {
                    // Ручной обмен элементов
                    PairVotes temp = list.get(i); // Сохраняем текущий элемент
                    list.set(i, list.get(i + 1)); // Заменяем текущий элемент на следующий
                    list.set(i + 1, temp); // Устанавливаем следующий элемент на место текущего
                    s = true; //  обмен произошел
                }
            }
            n--; // Уменьшаем n, так как последний элемент уже на месте
        } while (s); // Продолжаем, пока были обмены
    }
}
