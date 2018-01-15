package com.phpdaddy.eshopibm.repository;


import com.phpdaddy.eshopibm.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
