package com.phpdaddy.eshopibm.repository;


import com.phpdaddy.eshopibm.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
}
