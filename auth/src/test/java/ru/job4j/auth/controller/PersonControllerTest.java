package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.PersonService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService service;

    @Test
    public void whenReadAllPersonsSuccessfullyThenHttpStatusIsOk() throws Exception {
        when(service.readAll()).thenReturn(List.of(Person.of("test", "test")));
        String expected = "{\"id\":0,\"login\":\"test\",\"password\":\"test\"}";
        this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenReadAllPersonsUnsuccessfullyThenHttpStatusIsNotFound() throws Exception {
        when(service.readAll()).thenReturn(List.of());
        this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreatePersonSuccessfullyThenHttpStatusIsCreated() throws Exception {
        Person person = Person.of("test", "test");
        when(service.create(Mockito.any())).thenReturn(person);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cont = ow.writeValueAsString(person);
        String expected = "{\"id\":0,\"login\":\"test\",\"password\":\"test\"}";
        this.mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cont))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenReadPersonByIdSuccessfullyThenHttpStatusIsOk() throws Exception {
        when(service.read(anyInt())).thenReturn(Optional.of(Person.of("test", "test")));
        String expected = "{\"id\":0,\"login\":\"test\",\"password\":\"test\"}";
        this.mockMvc.perform(get("/person/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenReadPersonByIdUnsuccessfullyThenHttpStatusIsNotFound() throws Exception {
        when(service.read(0)).thenReturn(Optional.of(Person.of("test", "test")));
        this.mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdatePersonSuccessfullyThenHttpStatusIsOk() throws Exception {
        Person person = Person.of("test", "test");
        when(service.update(person)).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cont = ow.writeValueAsString(person);
        this.mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cont))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdatePersonUnsuccessfullyThenHttpStatusIsNotModified() throws Exception {
        Person person = Person.of("test", "test");
        when(service.update(person)).thenReturn(false);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cont = ow.writeValueAsString(person);
        this.mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cont))
                .andDo(print())
                .andExpect(status().isNotModified());
    }

    @Test
    public void whenDeletePersonSuccessfullyThenHttpStatusIsOk() throws Exception {
        when(service.delete(anyInt())).thenReturn(true);
        this.mockMvc.perform(delete("/person/0"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeletePersonUnsuccessfullyThenHttpStatusIsNotModified() throws Exception {
        when(service.delete(0)).thenReturn(false);
        this.mockMvc.perform(delete("/person/1"))
                .andDo(print())
                .andExpect(status().isNotModified());
    }
}
