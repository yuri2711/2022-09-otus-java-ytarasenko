package ru.otus;

import ru.otus.atm.ATM;
import ru.otus.atm.ATMImpl;
import ru.otus.atm.Banknote;

import java.util.Map;

import static ru.otus.atm.Banknote.*;

public class Main {
    public static void main(String[] args) {
        final ATM atm = new ATMImpl(Banknote.values());

        System.out.println(" *** Пытаемся получить деньги при нулевом балансе *** ");
        System.out.println("Balance = " + atm.getBalance());
        try {
            atm.getMoney(1000);
        } catch (IllegalArgumentException ex) {
            System.out.println("ERROR: " + ex.getMessage() + "\n");
        }

        System.out.println(" *** Вносим 45000 рублей *** ");
        final int resultSum = atm.addMoney(Map.of(
                HUNDRED, 100,
                TWO_HUNDRED, 100,
                FIVE_HUNDRED, 10,
                THOUSAND, 10));
        System.out.println("Внесено " + resultSum + " рублей");

        System.out.println("Balance = " + atm.getBalance() + "\n");

        System.out.println(" *** Получаем некорректную сумму (в современных банкоматах 50р уже недоступны) *** ");
        try {
            atm.getMoney(150);
        } catch (IllegalArgumentException ex) {
            System.out.println("ERROR: " + ex.getMessage() + "\n");
        }

        System.out.println(" *** Пытаемся снять больше, чем есть на счете *** ");
        try {
            atm.getMoney(50000);
        } catch (IllegalArgumentException ex) {
            System.out.println("ERROR: " + ex.getMessage() + "\n");
        }

        System.out.println(" *** Смотрим какими купюрами выдает банкомат разные суммы. В приоритете на выдачу крупные купюры при наличии *** ");
        for (int i = 1800; i >= 100; i -= 100) {
            getMoney(atm, i);
        }
    }

    private static void getMoney(ATM atm, int amount) {
        // получаем корректную сумму
        final var banknotes = atm.getMoney(amount);
        System.out.print("Выдано " + amount + " рублей: { ");
        for (var item : banknotes.entrySet()) {
            System.out.print(item.toString() + " ");
        }
        System.out.println("} \nBalance = " + atm.getBalance() + "\n");

    }
}
