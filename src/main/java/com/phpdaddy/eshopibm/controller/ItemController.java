package com.phpdaddy.eshopibm.controller;


import com.phpdaddy.eshopibm.model.Item;
import com.phpdaddy.eshopibm.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController extends CrudController<Item, Integer> {

    @Autowired
    public ItemController(ItemRepository repo) {
        super(repo);
    }
}