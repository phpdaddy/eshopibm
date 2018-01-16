package com.phpdaddy.eshopibm.controller;

import com.phpdaddy.eshopibm.exception.WebApplicationException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CrudController<T, ID extends Serializable> {

    private CrudRepository<T, ID> crudRepository;


    public CrudController(CrudRepository<T, ID> repo) {
        this.crudRepository = repo;
    }

    @RequestMapping(method = RequestMethod.GET)
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
    public ResponseEntity<?> get(@PathVariable ID id) throws WebApplicationException {
        Optional<T> byId = this.crudRepository.findById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        }
        throw new WebApplicationException("Entity does not exist ", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody T json) throws Exception {
        Optional<T> entity = this.crudRepository.findById(id);
        if (!entity.isPresent()) {
            throw new WebApplicationException("Entity does not exist ", HttpStatus.BAD_REQUEST);
        }
        try {
            BeanUtils.copyProperties(entity.get(), json);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        T updated = this.crudRepository.save(entity.get());

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