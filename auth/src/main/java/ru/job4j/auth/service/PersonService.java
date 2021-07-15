package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public Person create(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public boolean update(Person person) {
        if (read(person.getId()).isEmpty()) {
            return false;
        }
        personRepository.save(person);
        return true;
    }

    @Transactional
    public boolean delete(int id) {
        var person = read(id);
        if (person.isEmpty()) {
            return false;
        }
        personRepository.delete(person.get());
        return true;
    }

    public List<Person> readAll() {
        return (List<Person>) personRepository.findAll();
    }

    public Optional<Person> read(int id) {
        return personRepository.findById(id);
    }
}
