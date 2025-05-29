package br.com.rest.integration.controller;

import br.com.rest.config.TestConfigs;
import br.com.rest.integrationstests.testcontainers.AbstractIntegrationTest;
import br.com.rest.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.specification.RequestSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("Leandro", "Costa", "leandro@gmail.com", "Uberlândia - MG - BR", "Male");
    }

    @Test
    @Order(1)
    @DisplayName("Integration test given Person Object when create one person should return a Person Object")
    void integrationTestGivenPersonObject_when_CreateOnePerson_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person entity = mapper.readValue(content, Person.class);
        person = entity;

        assertNotNull(entity);
        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertNotNull(person.getEmail());

        assertTrue(person.getId() > 0);
        assertEquals("Leandro", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
        assertEquals("Uberlândia - MG - BR", entity.getAddress());
        assertEquals("Male", entity.getGender());
        assertEquals("leandro@gmail.com", entity.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("integration test given Person Object when update one Person should return a updated PersonObject")
    public void integrationTestGivenPersonObject_when_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws JsonProcessingException {

        person.setFirstName("Alberto");
        person.setEmail("alberto@gmail.com");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person entity = mapper.readValue(content, Person.class);
        person = entity;

        assertNotNull(entity);
        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertNotNull(person.getEmail());

        assertTrue(person.getId() > 0);
        assertEquals("Alberto", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
        assertEquals("Uberlândia - MG - BR", entity.getAddress());
        assertEquals("Male", entity.getGender());
        assertEquals("alberto@gmail.com", entity.getEmail());

    }

    @Test
    @Order(3)
    @DisplayName("integration test given Person Object when findById should return a Person Object")
    public void integrationTestGivenPersonObject_when_findById_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person entity = mapper.readValue(content, Person.class);

        assertNotNull(entity);
        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertNotNull(person.getEmail());

        assertTrue(person.getId() > 0);
        assertEquals("Alberto", entity.getFirstName());
        assertEquals("Costa", entity.getLastName());
        assertEquals("Uberlândia - MG - BR", entity.getAddress());
        assertEquals("Male", entity.getGender());
        assertEquals("alberto@gmail.com", entity.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("integration test when findAlll should return a Persons List")
    public void integrationTest_when_findBAll_ShouldReturnAPersonsList() throws JsonProcessingException {

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<Person> persons = Arrays.asList(mapper.readValue(content, Person[].class));

        assertFalse(persons.isEmpty());

        assertNotNull(persons.getFirst().getId());
        assertNotNull(persons.getFirst().getFirstName());
        assertNotNull(persons.getFirst().getLastName());
        assertNotNull(persons.getFirst().getAddress());
        assertNotNull(persons.getFirst().getGender());
        assertNotNull(persons.getFirst().getEmail());

        assertTrue(persons.getFirst().getId() > 0);
        assertEquals("Alberto", persons.getFirst().getFirstName());
        assertEquals("Costa", persons.getFirst().getLastName());
        assertEquals("Uberlândia - MG - BR", persons.getFirst().getAddress());
        assertEquals("Male", persons.getFirst().getGender());
        assertEquals("alberto@gmail.com", persons.getFirst().getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("integration test given Person Object when delete should return a no content")
    public void integrationTestGivenPersonObject_when_delete_ShouldReturnANoContent() {
        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }

}
