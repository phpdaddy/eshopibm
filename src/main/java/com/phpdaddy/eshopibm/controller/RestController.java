package com.phpdaddy.eshopibm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class RestController<T, ID extends Serializable> {

    private CrudRepository<T, ID> crudRepository;


    public RestController(CrudRepository<T, ID> repo) {
        this.crudRepository = repo;
    }

    @RequestMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(this.crudRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody T json) {

        T created = this.crudRepository.save(json);

        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        m.put("created", created);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable ID id) {
        return new ResponseEntity<>(this.crudRepository.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody T json) throws Exception {
        T entity = (T) this.crudRepository.findById(id);
        try {
            BeanUtils.copyProperties(entity, json);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        T updated = this.crudRepository.save(entity);

        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        m.put("id", id);
        m.put("updated", updated);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable ID id) {
        this.crudRepository.deleteById(id);
        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        return new ResponseEntity<>(m, HttpStatus.OK);
    }
}