package com.giogar.model;

import lombok.Data;

@Data
public class Customer {

    private String name;

    private String lastName;

    private String phoneNumber;

    private Address residentialAddress;

    private Address preferredDispatchAddress;

}
