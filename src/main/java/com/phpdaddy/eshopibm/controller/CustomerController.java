package com.phpdaddy.eshopibm.controller;


import com.phpdaddy.eshopibm.model.Customer;
import com.phpdaddy.eshopibm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController extends CrudController<Customer, Integer> {

    @Autowired
    public CustomerController(CustomerRepository repo) {
        super(repo);
    }
}