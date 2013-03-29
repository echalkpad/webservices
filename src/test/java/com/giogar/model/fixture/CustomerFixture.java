package com.giogar.model.fixture;

/**
 * Created with IntelliJ IDEA.
 * User: giovanni
 * Date: 29/03/13
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class CustomerFixture {

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
