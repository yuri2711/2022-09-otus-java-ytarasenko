package ru.otus.atm;

import ru.otus.atm.cell.Cell;
import ru.otus.atm.cell.CellImpl;

import java.util.*;

public class ATMImpl implements ATM {

    private final List<Cell> banknoteStore = new ArrayList<>();
    private int balance = 0;


    public ATMImpl(Banknote[] banknotes) {
        this.banknoteStore.addAll(
                Arrays.stream(banknotes)
                .map(CellImpl::new)
                .toList()
        );
    }


    @Override
    public Map<Banknote, Integer> getMoney(int number) {
        if (balance < number) {
            throw new IllegalArgumentException("Запрошенная сумма " + number + " рублей не может быть выдана. Баланс " + balance + " рублей");
        }
        if (number % 100 != 0) {
            throw new IllegalArgumentException("Запрошенная сумма " + number + " рублей не может быть выдана. Введите сумму, кратную 100");
        }
        balance -= number;
        return processGet(number);
    }

    @Override
    public int addMoney(Map<Banknote, Integer> banknotes) {
        final int amount = processAdd(banknotes);
        balance += amount;
        return amount;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    private Map<Banknote, Integer> processGet(int amount) {
        final var result = new HashMap<Banknote, Integer>();

        for (Cell cell : banknoteStore) {
            int count = 0;
            var banknote = cell.getBanknoteType();

            while (amount >= banknote.getDenomination() && cell.getNumber() > count) {
                amount -= cell.getMoney();
                count++;
            }
            result.put(banknote, count);
        }
        return result;
    }

    private int processAdd(Map<Banknote, Integer> banknotes) {
        int result = 0;

        for (Cell cell : banknoteStore) {
            if (banknotes.containsKey(cell.getBanknoteType())) {
                result += cell.addMoney(banknotes.get(cell.getBanknoteType()));
            }
        }
        return result;
    }
}
