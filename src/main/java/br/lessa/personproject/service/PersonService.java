package br.lessa.personproject.service;

import br.lessa.personproject.controller.PersonController;
import br.lessa.personproject.dto.PersonDTO;
import br.lessa.personproject.exception.RequiredObjectIsNullException;
import br.lessa.personproject.exception.ResourceNotFoundException;
import br.lessa.personproject.model.Person;
import br.lessa.personproject.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static br.lessa.personproject.mapper.ObjectMapper.parseListObjects;
import static br.lessa.personproject.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        addHeateOasLinks(dto);
        return dto;
    }

    public List<PersonDTO> findAll() {

        logger.info("Finding all persons");

        var people = parseListObjects(personRepository.findAll(), PersonDTO.class);
        people.forEach(this::addHeateOasLinks);
        return people;
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);

        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHeateOasLinks(dto);
        return dto;
    }

    public void delete(Long id) {

        var personExists = personRepository.existsById(id);
        if (personExists) {
            logger.info("Deleting Person " + id);
            personRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No resource found for this ID");
        }
    }

    public PersonDTO update(Long id, PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        Person personSaved = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        logger.info("Updating Person with id");

        personSaved.setFirstName(person.getFirstName());
        personSaved.setLastName(person.getLastName());
        personSaved.setAddress(person.getAddress());
        personSaved.setGender(person.getGender());

        var dto = parseObject(personRepository.save(personSaved), PersonDTO.class);
        addHeateOasLinks(dto);
        return dto;

    }
    private  void addHeateOasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class).deletePerson(dto.getId())).withRel("delete").withType("DELETE"));

        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("all").withType("GET") );

        dto.add(linkTo(methodOn(PersonController.class).createPerson(dto)).withRel("create").withType("POST"));

        dto.add(linkTo(methodOn(PersonController.class).updatePerson(dto.getId(), dto)).withRel("update").withType("PUT"));

    }

}
