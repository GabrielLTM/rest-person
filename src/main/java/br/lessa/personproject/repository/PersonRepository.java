package br.lessa.personproject.repository;

import br.lessa.personproject.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {
}
