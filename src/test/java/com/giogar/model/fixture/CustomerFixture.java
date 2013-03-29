package com.giogar.model.fixture;

import com.giogar.model.Address;
import com.giogar.model.Customer;
import com.giogar.model.Item;
import com.giogar.model.State;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: giovanni
 * Date: 29/03/13
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class CustomerFixture {

    public static Customer aCustomer() {
        Customer customer = new Customer();
        customer.setName("Giovanni");
        customer.setLastName("Gargiulo");
        customer.setPhoneNumber("+353 86 370 13 XX");
        return customer;
    }

    public static Address homeAddress() {
        Address address = new Address();
        address.setLineAddress1("Apt 1 - Dublin Court");
        address.setLineAddress2("Patrick St");
        address.setLineAddress3("Dublin 8");
        address.setState(State.IRELAND);
        return address;
    }

    public static Address officeAddress() {
        Address address = new Address();
        address.setLineAddress1("My Company PLC");
        address.setLineAddress2("Block 3 - Clonskeagh Rd");
        address.setLineAddress3("Dublin 12");
        address.setState(State.IRELAND);
        return address;
    }

    public static Item anItem() {
        Item item = new Item();
        item.setId(new Random().nextInt() % 100);
        item.setDescription("This is a description for an item");
        item.setQuantity(new Random().nextInt() % 10);
        return item;
    }

    public static com.giogar.model.version1_0.Customer aCustomerV1() {
        com.giogar.model.version1_0.Customer customer = new com.giogar.model.version1_0.Customer();
        customer.setId(1);
        customer.setName("Giovanni");
        customer.setLastName("Gargiulo");
        return customer;
    }

    public static com.giogar.model.version2_0.Customer aCustomerV2() {
        com.giogar.model.version2_0.Customer customer = new com.giogar.model.version2_0.Customer();
        customer.setId(1);
        customer.setName("Giovanni");
        customer.setLastName("Gargiulo");
        customer.setPhoneNumber("+353 86 370 13 XX");
        return customer;
    }

}
