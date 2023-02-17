package ru.otus.atm.cell;

import ru.otus.atm.Banknote;

public class CellImpl implements Cell {

    private final Banknote banknote;
    private int number = 0;


    public CellImpl(Banknote banknote) {
        this.banknote = banknote;
    }


    @Override
    public int addMoney(int number) {
        this.number += number;
        return number * banknote.getDenomination();
    }

    @Override
    public int getMoney() {
        this.number--;
        return banknote.getDenomination();
    }

    @Override
    public Banknote getBanknoteType() {
        return banknote;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
