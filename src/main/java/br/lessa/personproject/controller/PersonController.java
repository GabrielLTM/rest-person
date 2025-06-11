package br.lessa.personproject.controller;

import br.lessa.personproject.dto.PersonDTO;
import br.lessa.personproject.model.Person;
import br.lessa.personproject.service.PersonService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO findById(@PathVariable("id") String id){
        return personService.findById(id);
    }

    @GetMapping
    public List<PersonDTO> findAll(){
        return personService.findAll();
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody PersonDTO person){
        return personService.create(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable("id") String id){
         personService.delete(id);
         return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public PersonDTO updatePerson(@PathVariable("id") String id, @RequestBody PersonDTO person){
        return personService.update(id, person);
    }


}
