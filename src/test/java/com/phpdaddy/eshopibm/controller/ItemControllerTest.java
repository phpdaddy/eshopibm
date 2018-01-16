package com.phpdaddy.eshopibm.controller;

import com.phpdaddy.eshopibm.AppConfig;
import com.phpdaddy.eshopibm.controller.ItemController;
import com.phpdaddy.eshopibm.model.Customer;
import com.phpdaddy.eshopibm.model.Item;
import com.phpdaddy.eshopibm.model.Order;
import com.phpdaddy.eshopibm.repository.ItemRepository;
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
public class ItemControllerTest {

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(itemController)
                .build();
    }

    @Test
    public void listAllTest() throws Exception {
        Item first = this.createItem();

        Item second = new Item();
        second.setId(2);
        second.setName("item2");
        second.setPrice(123);
        second.setOrder(createOrder());

        List<Item> orders = Arrays.asList(first, second);

        when(itemRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(itemRepository, times(1)).findAll();
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void createTest() throws Exception {
        Item first = this.createItem();

        when(itemRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(itemRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(itemRepository);
    }


    @Test
    public void getTest() throws Exception {
        Item first = this.createItem();

        when(itemRepository.findById(1)).thenReturn(Optional.of(first));

        mockMvc.perform(get("/items/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(itemRepository, times(1)).findById(Matchers.any());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void updateTest() throws Exception {
        Item first = this.createItem();

        when(itemRepository.findById(1)).thenReturn(Optional.of(first));
        when(itemRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/items/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(itemRepository, times(1)).findById(Matchers.any());
        verify(itemRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(itemRepository, times(1)).deleteById(Matchers.any());
        verifyNoMoreInteractions(itemRepository);
    }


    private Item createItem() {
        Item first = new Item();
        first.setId(1);
        first.setName("item1");
        first.setPrice(123);
        first.setOrder(createOrder());
        return first;
    }

    private Order createOrder() {
        Order first = new Order();
        first.setId(1);
        first.setCustomer(this.createCustomer());
        return first;
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