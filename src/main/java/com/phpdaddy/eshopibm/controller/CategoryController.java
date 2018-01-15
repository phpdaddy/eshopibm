package com.phpdaddy.eshopibm.controller;


import com.phpdaddy.eshopibm.model.Category;
import com.phpdaddy.eshopibm.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController extends CrudController<Category, Integer> {

    @Autowired
    public CategoryController(CategoryRepository repo) {
        super(repo);
    }
}