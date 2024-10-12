package ru.demo.lockdemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final Lock lock = new ReentrantLock();//ReentrantLock избегание взяимных блокировок;
    // обеспечивает неизменность данной блокировки.
    private final Condition Balance = lock.newCondition();// компонент для управления ожиданиями потоков.

    private long balance; // Текущий баланс счёта

    // Конструктор класса, принимает начальный баланс
    public Account(long initialBalance) {
        this.balance = initialBalance;
    }

    // Метод для получения текущего баланса, защищённый блокировкой
    public long getBalance() {
        lock.lock();// Захватываем блокировку перед доступом к ресурсу
        try {
            return balance;
        } finally {
            lock.unlock();//освобождаем блокировку
        }
    }

    // Метод для пополнения баланса
    public void deposit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        lock.lock();//блокируем
        try {
            balance += amount;
            Balance.signalAll(); //Уведомляем все потоки, ожидающие изменения баланса
        } finally {
            lock.unlock();// Освобождаем блокировку
        }
    }

    // Метод для снятия денег со счета
    public void www(long amount) throws InterruptedException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма вывода должна быть положительной");
        }
        lock.lock();
        try {
            while (balance < amount) {
                Balance.await(); // Ожидаем, пока баланс не станет достаточным
            }
            balance -= amount;// Снимаем деньги со счёта
        } finally {
            lock.unlock();// Освобождаем блокировку
        }
    }
}
//класс потока
class Deposit extends Thread {
    private final Account account;// Ссылка на объект банковского счёта

    // Конструктор, принимающий объект счёта
    public Deposit(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        while (true) {//цикл для постоянного пополнения
            try {
                // Генерируем случайную сумму для пополнения от 1 до 1000
                int depositAmount = (int) (Math.random() * 1000) + 1;
                account.deposit(depositAmount);
                System.out.println("Внесения " + depositAmount + ", Текущий баланс " + account.getBalance());

                // Пауза на случайный промежуток времени
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
                break;
            }
        }
    }
}
class Main {
    public static void main(String[] args) {
        Account account = new Account(0); // Начальный баланс 0

        // Запускаем поток для пополнения счета
        Deposit deposit = new Deposit(account);
        deposit.start();

        //  снять деньги с аккаунта через определенный промежуток времени
        try {
            // Ждём, пока баланс станет достаточным для снятия 3000
            account.www(3000);
            System.out.println("Вывод 3000 долларов прошел успешно. Остаток средств: " + account.getBalance());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Завершаем поток пополнения счета
        deposit.interrupt(); // Остановка потока
        try {
            deposit.join(); // Ожидание завершения потока
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
