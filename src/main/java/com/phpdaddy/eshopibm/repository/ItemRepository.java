package com.phpdaddy.eshopibm.repository;


import com.phpdaddy.eshopibm.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
}
