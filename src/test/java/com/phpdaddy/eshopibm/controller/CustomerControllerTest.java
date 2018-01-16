package com.phpdaddy.eshopibm.controller;

import com.phpdaddy.eshopibm.AppConfig;
import com.phpdaddy.eshopibm.controller.CustomerController;
import com.phpdaddy.eshopibm.exception.ExceptionControllerAdvice;
import com.phpdaddy.eshopibm.model.Customer;
import com.phpdaddy.eshopibm.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private CustomerController customerController;
    @Mock
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(customerController)
                .build();
    }

    @Test
    public void listAllTest() throws Exception {
        Customer first = this.createCustomer();

        Customer second = new Customer();
        second.setId(2);
        second.setEmail("email2");
        second.setFirstName("firstName2");
        second.setLastName("lastName2");

        List<Customer> customers = Arrays.asList(first, second);

        when(customerRepository.findAll()).thenReturn(customers);

        mockMvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("email1")))
                .andExpect(jsonPath("$[0].firstName", is("firstName1")))
                .andExpect(jsonPath("$[0].lastName", is("lastName1")));

        verify(customerRepository, times(1)).findAll();
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void createTest() throws Exception {
        Customer first = this.createCustomer();

        when(customerRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(customerRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(customerRepository);
    }


    @Test
    public void getTest() throws Exception {
        Customer first = this.createCustomer();

        when(customerRepository.findById(1)).thenReturn(Optional.of(first));

        mockMvc.perform(get("/customers/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("email1")))
                .andExpect(jsonPath("$.firstName", is("firstName1")))
                .andExpect(jsonPath("$.lastName", is("lastName1")));

        verify(customerRepository, times(1)).findById(Matchers.any());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void updateTest() throws Exception {
        Customer first = this.createCustomer();

        when(customerRepository.findById(1)).thenReturn(Optional.of(first));
        when(customerRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/customers/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(customerRepository, times(1)).findById(Matchers.any());
        verify(customerRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(customerRepository, times(1)).deleteById(Matchers.any());
        verifyNoMoreInteractions(customerRepository);
    }

    private Customer createCustomer() {
        Customer first = new Customer();
        first.setId(1);
        first.setEmail("email1");
        first.setFirstName("firstName1");
        first.setLastName("lastName1");
        return first;
    }
}