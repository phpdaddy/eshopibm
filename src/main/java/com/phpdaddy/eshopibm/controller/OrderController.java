package com.phpdaddy.eshopibm.controller;


import com.phpdaddy.eshopibm.model.Order;
import com.phpdaddy.eshopibm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController extends CrudController<Order, Integer> {

    @Autowired
    public OrderController(OrderRepository repo) {
        super(repo);
    }
}