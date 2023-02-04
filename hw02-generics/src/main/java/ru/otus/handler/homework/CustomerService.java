package ru.otus.handler.homework;


import java.util.*;

public class CustomerService {

    private TreeMap<Customer, String> map = new TreeMap<>();
    private TreeMap<Customer, String> mapCopy = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        mapCopy.putAll(map);
        Map.Entry<Customer, String> cse = mapCopy.firstEntry();
        Customer customer = new Customer(cse.getKey().getId(), cse.getKey().getName(), cse.getKey().getScores());
        String value = cse.getValue();
        map.remove(customer);
        return new AbstractMap.SimpleEntry<>(customer, value); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Customer customer1 = map.ceilingKey(customer);
        if (customer1 == null) return null;
        Customer del = new Customer(customer1.getId(), customer1.getName(), customer1.getScores());
        AbstractMap.SimpleEntry<Customer, String> customerStringSimpleEntry = new AbstractMap.SimpleEntry<>(del, map.get(del));
        map.remove(del);
        return customerStringSimpleEntry; // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
