package br.com.rest.services;

import br.com.rest.exceptions.ResourceFoundException;
import br.com.rest.model.Person;
import br.com.rest.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    private Person person;

    @BeforeEach
    public void setup() {
        //Given
        person = new Person("Leandro", "Costa", "leandro@gmail.com", "Uberlândia - MG - BR", "Male");
    }

    @Test
    @DisplayName("Test given Person Object when save Person then return Person Object")
    void testGivenPersonObject_whenSavePerson_thenReturnPersonObject() {
        //Given
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person)).willReturn(person);

        //When
        Person entity = service.create(person);

        //Then
        assertNotNull(entity);
        assertEquals("Leandro", entity.getFirstName());
    }

    @Test
    @DisplayName("Test given existing email when save Person then throws Exception")
    void testGivenExistingEmail_whenSavePerson_thenThrowsException() {
        //Given
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person));

        //When
        assertThrows(ResourceFoundException.class, () -> service.create(person));

        //Then
        verify(repository, never()).save(any(Person.class));
    }

    @Test
    @DisplayName("Test Given Persons List When Find All Persons then Return List of Persons")
    void testGivenPersonsList_whenFindAllPersons_thenReturnListOfPersons() {
        //Given
        given(repository.findAll()).willReturn(List.of(person));

        //When
        List<Person> persons = service.findAll();;

        //Then
        assertNotNull(persons);
        assertFalse(persons.isEmpty());
    }

    @Test
    @DisplayName("Test Given Persons empty List When Find All Persons then Return empty List of Persons")
    void testGivenPersonsEmptyList_whenFindAllPersons_thenReturnEmptyListOfPersons() {
        //Given
        given(repository.findAll()).willReturn(Collections.emptyList());

        //When
        List<Person> persons = service.findAll();

        //Then
        assertTrue(persons.isEmpty());
    }

    @Test
    @DisplayName("Test given Person id Object when findById then return Person Object")
    void testGivenPersonId_whenfindById_thenReturnPersonObject() {
        //Given
        given(repository.findById(anyLong())).willReturn(Optional.of(person));

        //When
        Person entity = service.findById(1L);

        //Then
        assertNotNull(entity);
        assertEquals("Leandro", entity.getFirstName());
    }

    @Test
    @DisplayName("Test given Person Object when update then return updated Person Object")
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
        //Given
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        person.setId(1L);
        person.setFirstName("Leonardo");
        person.setLastName("Beltrano");

        given(repository.save(person)).willReturn(person);

        //When
        Person entity = service.update(person);

        //Then
        assertNotNull(entity);
        assertEquals("Leonardo", entity.getFirstName());
        assertEquals("Beltrano", entity.getLastName());
    }

    @Test
    @DisplayName("Test given Person Id Object when delete Person the do nothing")
    void testGivenPersonId_whenDeletePerson_thenDoNothing() {
        //Given
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        person.setId(1L);
        willDoNothing().given(repository).delete(person);

        //When
        service.delete(person.getId());

        //Then
        verify(repository, times(1)).delete(person);
    }

}