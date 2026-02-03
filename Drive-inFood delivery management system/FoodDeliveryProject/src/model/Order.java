package model;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private Customer customer;
    private Map<MenuItem, Integer> items;

    public Order(Customer customer) {
        this.customer = customer;
        this.items = new HashMap<>();
    }

    public void addItem(MenuItem item, int qty) {
        items.put(item, items.getOrDefault(item, 0) + qty);
    }

    public Map<MenuItem, Integer> getItems() { return items; }
    public Customer getCustomer() { return customer; }
}
