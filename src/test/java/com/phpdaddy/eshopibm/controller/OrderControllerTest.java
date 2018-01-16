package com.phpdaddy.eshopibm.controller;

import com.phpdaddy.eshopibm.AppConfig;
import com.phpdaddy.eshopibm.controller.OrderController;
import com.phpdaddy.eshopibm.model.Customer;
import com.phpdaddy.eshopibm.model.Order;
import com.phpdaddy.eshopibm.repository.OrderRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(orderController)
                .build();
    }

    @Test
    public void listAllTest() throws Exception {
        Order first = this.createOrder();

        Order second = new Order();
        second.setId(2);
        second.setCustomer(this.createCustomer());

        List<Order> orders = Arrays.asList(first, second);

        when(orderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void createTest() throws Exception {
        Order first = this.createOrder();

        when(orderRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(orderRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(orderRepository);
    }


    @Test
    public void getTest() throws Exception {
        Order first = this.createOrder();

        when(orderRepository.findById(1)).thenReturn(Optional.of(first));

        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(orderRepository, times(1)).findById(Matchers.any());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void updateTest() throws Exception {
        Order first = this.createOrder();

        when(orderRepository.findById(1)).thenReturn(Optional.of(first));
        when(orderRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(orderRepository, times(1)).findById(Matchers.any());
        verify(orderRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void deleteTest() throws Exception {

        mockMvc.perform(delete("/orders/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(orderRepository, times(1)).deleteById(Matchers.any());
        verifyNoMoreInteractions(orderRepository);
    }

    private Customer createCustomer() {
        Customer first = new Customer();
        first.setId(1);
        first.setEmail("email1");
        first.setFirstName("firstName1");
        first.setLastName("lastName1");
        return first;
    }

    private Order createOrder() {
        Order first = new Order();
        first.setId(1);
        first.setCustomer(this.createCustomer());
        return first;
    }

}