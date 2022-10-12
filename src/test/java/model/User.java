package model;

import java.util.List;

public class User {
    public String name;
    public int age;
    public String country;
    public String[] hobby;
    public Order order;

    public static class Order {
            public String orderDate;
            public int products;
            public boolean isCreditCard;
            public List <String> productsDetail;

    }
}
