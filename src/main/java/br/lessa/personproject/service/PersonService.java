package br.lessa.personproject.service;

import br.lessa.personproject.exception.ResourceNotFoundException;
import br.lessa.personproject.model.Person;
import br.lessa.personproject.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    public Person findById(String id) {
        logger.info("Finding one person!");
        return personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

    }

    public List<Person> findAll() {

        logger.info("Finding all persons");

        return personRepository.findAll();
    }

    public Person create(Person person) {

        logger.info("Creating one Person!");

        return personRepository.save(person);
    }

    public void delete(String id) {

        Long personId = Long.parseLong(id);

        var personExists = personRepository.existsById(personId);
        if (personExists) {
            logger.info("Deleting Person " + id);
            personRepository.deleteById(personId);
        }else{
            throw new ResourceNotFoundException("No resource found for this ID");
        }
    }

    public Person update(String id, Person person) {

        var longId = Long.parseLong(id);

        var personSaved = personRepository.findById(longId).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        logger.info("Updating Person with id");

        personSaved.setFirstName(person.getFirstName());
        personSaved.setLastName(person.getLastName());
        personSaved.setAddress(person.getAddress());
        personSaved.setGender(person.getGender());

        return personRepository.save(personSaved);


    }

}
