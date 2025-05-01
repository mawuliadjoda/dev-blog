package com.esprit.batch.config;

import com.esprit.batch.persistence.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        // if(customer.getCountry().equals("Nigeria"))
        //     return customer;
        // return null;
        return customer;
    }
}
