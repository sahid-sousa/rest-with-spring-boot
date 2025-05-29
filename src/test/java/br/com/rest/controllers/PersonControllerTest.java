package br.com.rest.controllers;

import br.com.rest.exceptions.ResourceNotFoundException;
import br.com.rest.model.Person;
import br.com.rest.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService service;

    private Person person;

    @BeforeEach
    public void setup() {
        //Given
        person = new Person("Leandro", "Costa", "leandro@gmail.com", "Uberlândia - MG - BR", "Male");
    }

    @Test
    @DisplayName("Test Given Person Object When Create Person then Return Saved Person")
    void testGivenPersonObject_whenCreatePerson_thenReturnSavedPerson() throws Exception {
        // Given
        given(service.create(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));

        //Then
       response.andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
               .andExpect(jsonPath("$.lastName", is(person.getLastName())))
               .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Test Given List of Person when find all Persons then return Persons List")
    void testGivenListOfPerson_whenFindAllPersons_thenReturnPersonList() throws Exception {
        // Given

        List<Person> persons = new ArrayList<>();
        persons.add(person);

        given(service.findAll()).willReturn(persons);

        // When
        ResultActions response = mockMvc.perform(get("/person"));

        //Then
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(persons.size())));
    }

    @Test
    @DisplayName("Test Given Person id when findById then return Person")
    void testGivenPersonId_whenFindById_thenReturnPerson() throws Exception {
        // Given
        long personId = 1L;
        given(service.findById(personId)).willReturn(person);

        // When
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        //Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Test Given invalid Person id when findById then return not found")
    void testGivenInvalidPersonId_whenFindById_thenReturnNotFound() throws Exception {
        // Given
        long personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);

        // When
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));

        //Then
        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    @DisplayName("Test Given unexistent Person when update then return not found")
    void testGivenUnexistentPerson_whenUpdate_thenReturnNotFound() throws Exception {
        // Given
        long personId = 1L;

        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(service.update(any(Person.class))).willAnswer(invocation -> invocation.getArgument(1));

        // When
        Person update = new Person("Adriano", "Pereira", "pereirao@gmail.com", "Uberlândia - MG - BR", "Male");
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        //Then
        response.
                andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Test Given Person id when delete then return not content")
    void testGivenPersonId_whenDelete_thenReturnNotContent() throws Exception {
        // Given
        long personId = 1L;

        willDoNothing().given(service).delete(personId);

        // When

        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        //Then
        response.
                andExpect(status().isNoContent())
                .andDo(print());
    }

}