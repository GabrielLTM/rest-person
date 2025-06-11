package br.lessa.personproject.service;

import br.lessa.personproject.dto.PersonDTO;
import br.lessa.personproject.exception.ResourceNotFoundException;

import static br.lessa.personproject.mapper.ObjectMapper.parseListObjects;
import static br.lessa.personproject.mapper.ObjectMapper.parseObject;

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

    public PersonDTO findById(String id) {
        logger.info("Finding one person!");
        var entity = personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return parseObject(entity, PersonDTO.class);
    }

    public List<PersonDTO> findAll() {

        logger.info("Finding all persons");

        return parseListObjects(personRepository.findAll(), PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {

        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);

        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    public void delete(String id) {

        Long personId = Long.parseLong(id);

        var personExists = personRepository.existsById(personId);
        if (personExists) {
            logger.info("Deleting Person " + id);
            personRepository.deleteById(personId);
        } else {
            throw new ResourceNotFoundException("No resource found for this ID");
        }
    }

    public PersonDTO update(String id, PersonDTO person) {

        var longId = Long.parseLong(id);

        Person personSaved = personRepository.findById(longId).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        logger.info("Updating Person with id");

        personSaved.setFirstName(person.getFirstName());
        personSaved.setLastName(person.getLastName());
        personSaved.setAddress(person.getAddress());
        personSaved.setGender(person.getGender());

        return parseObject(personRepository.save(personSaved), PersonDTO.class);


    }

}
