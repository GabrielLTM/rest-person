package br.lessa.personproject.service;

import br.lessa.personproject.dto.PersonDTO;
import br.lessa.personproject.exception.RequiredObjectIsNullException;
import br.lessa.personproject.mapper.mocks.MockPerson;
import br.lessa.personproject.model.Person;
import br.lessa.personproject.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService personService;

    @Mock
    PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {

        Person person = input.mockEntity(1);
        person.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        var result = personService.findById(1L);

        assertEquals(1L, result.getId() );
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("all")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")));




        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    void create() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);


        when(personRepository.save(person)).thenReturn(persisted);

        PersonDTO personDTO = input.mockDTO(1);

        var result = personService.create(personDTO);

        assertEquals(1L, result.getId() );
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("all")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")));

    }


    @Test
    void delete() {

        Person person = input.mockEntity(1);
        person.setId(1L);

        when(personRepository.existsById(1L)).thenReturn(true);

        personService.delete(1L);

        verify(personRepository).deleteById(person.getId() );
    }

    @Test
    void testCreateWithNullPerson(){
        Exception e = assertThrows(RequiredObjectIsNullException.class,
                () -> {personService.create(null);}
        );

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void update() {

        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);


        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(persisted);

        PersonDTO personDTO = input.mockDTO(1);

        var result = personService.update(personDTO.getId(), personDTO);

        assertEquals(1L, result.getId() );
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")));

        assertTrue(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("all")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")));
    }

    @Test
    void testUpdateWithNullPerson(){
        Exception e = assertThrows(RequiredObjectIsNullException.class,
                () -> {personService.update(null, null);}
        );

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = e.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void findAll() {
        List<Person> persons = input.mockEntityList();
        when(personRepository.findAll()).thenReturn(persons);

        List<PersonDTO> result = personService.findAll();

        var personOne = result.get(1);

        assertEquals(14, result.size());
        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("DELETE")));

        assertTrue(personOne.getLinks().stream().anyMatch(link -> link.getRel().value().equals("all")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")));
    }
}