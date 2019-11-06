package com.roche.poc.controller;

import com.roche.poc.entity.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("person")
public class PersonController {

    @PostMapping
    int savePerson(@RequestBody Person person) {
        return 204;
    }

    @GetMapping("/{id}")
    ResponseEntity<Person> getPerson(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new Person(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePerson(@PathVariable("id") Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
