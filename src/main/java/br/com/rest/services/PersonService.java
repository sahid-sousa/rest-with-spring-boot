package br.com.rest.services;

import br.com.rest.exceptions.ResourceFoundException;
import br.com.rest.exceptions.ResourceNotFoundException;
import br.com.rest.model.Person;
import br.com.rest.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PersonService {

    PersonRepository repository;

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        logger.info("Find all person ");
        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Find person by id: " + id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));
    }

    public Person create(Person person) {
        logger.info("Create person");

        Optional<Person> entity = repository.findByEmail(person.getEmail());
        if (entity.isPresent()) throw new ResourceFoundException("Person already exist with given email: " + person.getEmail());
        return repository.save(person);
    }

    public Person update(Person person) {
        Long id = person.getId();

        logger.info("Update person with id: " + id);
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setEmail(person.getEmail());
        entity.setGender(person.getGender());

        return repository.save(entity);
    }

    public void delete(Long id)  {
        logger.info("Delete person with id: " + id);
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found for id: " + id));
        repository.delete(entity);
    }

}
