package com.giogar.model;


import lombok.Data;

import java.util.List;

/**
 * @author ggargiulo
 */
@Data
public class Order {

    private int id;

    private Customer customer;

    private Address shippingAddress;

    private List<Item> items;

}
