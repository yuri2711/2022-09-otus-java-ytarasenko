package ru.otus.homework;


import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {
    private Deque<Customer> customerList = new LinkedList<>();
    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        boolean add = customerList.add(customer);
    }

    public Customer take() {
        Customer customer = customerList.pollLast();
        return customer; // это "заглушка, чтобы скомилировать"
    }
}
