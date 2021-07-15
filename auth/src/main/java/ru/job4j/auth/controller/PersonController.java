package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.PersonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(service.create(person), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        return service.update(person)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> readAll() {
       List<Person> persons = service.readAll();
       return !persons.isEmpty()
               ? ResponseEntity.ok(persons)
               : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> read(@PathVariable int id) {
        Optional<Person> person = service.read(id);
        return person.isPresent()
                ? ResponseEntity.ok(person.get())
                : ResponseEntity.notFound().build();
    }
}
