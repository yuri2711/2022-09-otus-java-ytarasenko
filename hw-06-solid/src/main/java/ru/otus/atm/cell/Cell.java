package ru.otus.atm.cell;

import ru.otus.atm.Banknote;

public interface Cell {

    /**
     * Добавить купюры в ячейку
     * @param number количество купюр
     * @return внесенная сумма
     */
    int addMoney(int number);

    /**
     * Взять купюры из ячейки
     * @return номинал взятой купюры
     */
    int getMoney();

    /**
     * @return тип банкноты
     */
    Banknote getBanknoteType();

    /**
     * @return количество купюр в ячейке
     */
    int getNumber();
}
