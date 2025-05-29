package br.com.rest.repositories;

import br.com.rest.integrationstests.testcontainers.AbstractIntegrationTest;
import br.com.rest.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository repository;

    private Person person;

    @BeforeEach
    public void setup() {
        //Given
        person = new Person("Leandro", "Costa", "leandro@gmail.com", "Uberlândia - MG - BR", "Male");
    }

    @Test
    @DisplayName("Given Person Object when save then return saved Person")
    void testGivenPersonObject_whenSavePerson_thenReturnSavedPerson() {
        //When
        Person entity = repository.save(person);
        //Then
        assertNotNull(entity);
        assertTrue(entity.getId() > 0);
    }

    @Test
    @DisplayName("Given Person List when findAll then return Person List")
    void testGivenPersonList_whenFindAll_thenReturnPersonList() {
        //Given
        repository.save(new Person("Alberto", "Costa", "leandro@gmail.com", "Uberlândia - MG - BR", "Male"));
        repository.save(new Person("Leandro", "Costa", "leandro@gmail.com", "Uberlândia - MG - BR", "Male"));
        //When
        List<Person> personList = repository.findAll();
        //Then
        assertNotNull(personList);
        assertFalse(personList.isEmpty());
    }

    @Test
    @DisplayName("Given Person Object when findById then return Person Object")
    void testGivenPersonObject_whenFindById_thenReturnPersonObject() {
        //Given
        Person p1 = repository.save(person);
        //When
        Optional<Person> entity = repository.findById(p1.getId());
        //Then
        assertTrue(entity.isPresent());
        assertEquals(p1.getId(), entity.get().getId());
    }

    @Test
    @DisplayName("Given Person Object when findByEmail then return Person Object")
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {
        //Given
        Person p1 = repository.save(person);
        //When
        Optional<Person> entity = repository.findByEmail(p1.getEmail());
        //Then
        assertTrue(entity.isPresent());
        assertEquals(p1.getId(), entity.get().getId());
    }

    @Test
    @DisplayName("Given Person Object when update Person then return updated Person Object")
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
        // Given
        Person p1 = repository.save(person);

        // When
        Person entity = repository.findById(p1.getId()).orElse(null);
        assertNotNull(entity);

        entity.setFirstName("Leonardo");
        entity.setLastName("Costa");
        entity.setEmail("leonardo@gmail.com");

        Person updatedPerson = repository.save(entity);

        // Then
        assertNotNull(updatedPerson);
        assertEquals(p1.getId(), updatedPerson.getId());
        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("Costa", updatedPerson.getLastName());
        assertEquals("leonardo@gmail.com", updatedPerson.getEmail());
    }

    @Test
    @DisplayName("Given Person Object when delete Person then remove Person")
    void testGivenPersonObject_whenDelete_thenRemovePerson() {
        // Given
        Person p1 = repository.save(person);

        // When
        Person entity = repository.findById(p1.getId()).orElse(null);
        assertNotNull(entity);

        repository.deleteById(entity.getId());
        Optional<Person> personOptional = repository.findById(p1.getId());

        // Then
        assertTrue(personOptional.isEmpty());
    }

    @Test
    @DisplayName("Given firstName and lastName when findByBJPQL then return Person Object")
    void testGivenFirstNameAndLastName_whenFindByJPQL_thenReturnPersonObject() {
        //Given
        Person p1 = repository.save(person);
        //When
        Person entity = repository.findByJPQL(p1.getFirstName(), p1.getLastName());
        //Then
        assertNotNull(entity);
        assertEquals(p1.getId(), entity.getId());
        assertEquals("Leandro", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
    }

    @Test
    @DisplayName("Given firstName and lastName when findByJPQLNameParameters then return Person Object")
    void testGivenFirstNameAndLastName_whenJPQLNameParameters_thenReturnPersonObject() {
        //Given
        Person p1 = repository.save(person);
        //When
        Person entity = repository.findByJPQLNameParameters(p1.getFirstName(), p1.getLastName());
        //Then
        assertNotNull(entity);
        assertEquals(p1.getId(), entity.getId());
        assertEquals("Leandro", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
    }

    @Test
    @DisplayName("Given firstName and lastName when findByNativeSQL then return Person Object")
    void testGivenFirstNameAndLastName_whenFindByNativeSQL_thenReturnPersonObject() {
        //Given
        Person p1 = repository.save(person);
        //When
        Person entity = repository.findByNativeSql(p1.getFirstName(), p1.getLastName());
        //Then
        assertNotNull(entity);
        assertEquals(p1.getId(), entity.getId());
        assertEquals("Leandro", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
    }

    @Test
    @DisplayName("Given firstName and lastName when findByNativeSQLWithNameParameters then return Person Object")
    void testGivenFirstNameAndLastName_whenFindByNativeSQLWithNameParameters_thenReturnPersonObject() {
        //Given
        Person p1 = repository.save(person);
        //When
        Person entity = repository.findByNativeSqlWithNameParameters(p1.getFirstName(), p1.getLastName());
        //Then
        assertNotNull(entity);
        assertEquals(p1.getId(), entity.getId());
        assertEquals("Leandro", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
    }


}