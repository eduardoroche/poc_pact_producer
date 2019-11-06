package com.roche.poc.controller;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roche.poc.config.Application;
import com.roche.poc.entity.Person;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRestPactRunner.class)
@Provider("person-provider")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@PactBroker(host = "${pactbroker.url}", port = "${pactbroker.port}", tags = "${pactbroker.tags:master}")
public class PersonControllerContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @State("A person is saved")
    public void shouldSavePersonAndReturnCreatedAsHttpStatus() throws Exception {

        //Arrange
        Person person = new Person();
        person.setName("Roche");
        //ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).build();

        //Act
        mockMvc.perform(post("/person")
                .content(objectMapper.writeValueAsString(person))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @State("A person is deleted with an existing id")
    public void shouldDeletePersonAndReturnOkAsHttpStatus() throws Exception {

        //Arrange
        Long id = 1l;
        ResponseEntity<Void> voidResponse = ResponseEntity.status(HttpStatus.OK).build();

        //Act
        mockMvc.perform(delete("/person/{id}", id))
                .andExpect(status().isOk());
        //when(personController.deletePerson(id)).thenReturn(voidResponse);

    }

    @State("A person is updated with an existing id")
    public void shouldUpdatePersonAndReturnNoContentAsHttpStatus() throws Exception  {

        //Arrange
        Long id = 1l;
        Person person = new Person();
        person.setId(id);
        person.setName("Roche");
        ResponseEntity<Void> voidResponse = Mockito.spy(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        //Act
        mockMvc.perform(put("/person")
                .content(objectMapper.writeValueAsString(person))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
       // when(personController.updatePerson(1l, person)).thenReturn(voidResponse);
       // when(voidResponse.getStatusCode()).thenReturn(HttpStatus.NO_CONTENT);

    }

    @State("A person is requested with an existing id")
    public void shouldReturnPersonAndReturnOkAsHttpStatus() throws Exception  {

        //Arrange
        Long id = 1l;
        Person person = new Person();
        person.setId(id);
        person.setName("Roche");

        //Act
        mockMvc.perform(get("/person/{id}", id)).andExpect(status().isOk()).andReturn();
        //when(personController.getPerson(id)).thenReturn(ResponseEntity.ok(person));

    }

}
