package com.phpdaddy.eshopibm.controller;

import com.phpdaddy.eshopibm.AppConfig;
import com.phpdaddy.eshopibm.controller.CategoryController;
import com.phpdaddy.eshopibm.model.Category;
import com.phpdaddy.eshopibm.model.Customer;
import com.phpdaddy.eshopibm.model.Item;
import com.phpdaddy.eshopibm.model.Order;
import com.phpdaddy.eshopibm.repository.CategoryRepository;
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
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(categoryController)
                .build();
    }

    @Test
    public void listAllTest() throws Exception {
        Category first = this.createCategory();

        Category second = new Category();
        second.setId(1);
        second.setName("category2");

        List<Category> orders = Arrays.asList(first, second);

        when(categoryRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void createTest() throws Exception {
        Category first = this.createCategory();

        when(categoryRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(categoryRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(categoryRepository);
    }


    @Test
    public void getTest() throws Exception {
        Category first = this.createCategory();

        when(categoryRepository.findById(1)).thenReturn(Optional.of(first));

        mockMvc.perform(get("/categories/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(categoryRepository, times(1)).findById(Matchers.any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void updateTest() throws Exception {
        Category first = this.createCategory();

        when(categoryRepository.findById(1)).thenReturn(Optional.of(first));
        when(categoryRepository.save(first)).thenReturn(first);

        mockMvc.perform(post("/categories/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(categoryRepository, times(1)).findById(Matchers.any());
        verify(categoryRepository, times(1)).save(Matchers.any());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(categoryRepository, times(1)).deleteById(Matchers.any());
        verifyNoMoreInteractions(categoryRepository);
    }


    private Category createCategory() {
        Category first = new Category();
        first.setId(1);
        first.setName("category1");
        return first;
    }
}