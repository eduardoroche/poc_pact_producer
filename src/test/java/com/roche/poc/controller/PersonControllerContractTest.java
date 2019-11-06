package com.roche.poc.controller;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import com.roche.poc.config.Application;
import com.roche.poc.entity.Person;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@Provider("person-provider")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@PactBroker(host = "${pactbroker.url}", port = "${pactbroker.port}", tags = "${pactbroker.tags:master}")
public class PersonControllerContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @MockBean
    private PersonController personController;

    @State("A person is saved")
    public void shouldSavePersonAndReturnCreatedAsHttpStatus() {

        //Arrange
        Person person = new Person();
        person.setName("Roche");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).build();

        //Act
        when(personController.savePerson(person)).thenReturn(response);

    }

    @State("A person is deleted with an existing id")
    public void shouldDeletePersonAndReturnOkAsHttpStatus() {

        //Arrange
        Long id = 1l;
        ResponseEntity<Void> voidResponse = ResponseEntity.status(HttpStatus.OK).build();

        //Act
        when(personController.deletePerson(id)).thenReturn(voidResponse);

    }

    @State("A person is updated with an existing id")
    public void shouldUpdatePersonAndReturnNoContentAsHttpStatus() {

        //Arrange
        Long id = 1l;
        Person person = new Person();
        person.setName("Roche");
        ResponseEntity<Void> voidResponse = Mockito.spy(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        //Act
        when(personController.updatePerson(1l, person)).thenReturn(voidResponse);
        when(voidResponse.getStatusCode().value()).thenReturn(204);

    }

    @State("A person is requested with an existing id")
    public void shouldReturnPersonAndReturnOkAsHttpStatus() {

        //Arrange
        Long id = 1l;
        Person person = new Person();
        person.setId(id);
        person.setName("Roche");

        //Act
        when(personController.getPerson(id)).thenReturn(ResponseEntity.ok(person));

    }

}
