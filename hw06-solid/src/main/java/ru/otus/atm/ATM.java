package ru.otus.atm;

import java.util.Map;

public interface ATM {

    /**
     * Выдача купюр
     * @param number запрашиваемая сумма
     * @return банкноты разного номинала
     */
    Map<Banknote, Integer> getMoney(int number);

    /**
     * Внесение денег
     * @param money принятая банкоматом сумма
     * @return принятая сумма
     */
    int addMoney(Map<Banknote, Integer> money);

    /**
     * Запрос баланса
     * @return остаток средств в банкомате
     */
    int getBalance();
}
