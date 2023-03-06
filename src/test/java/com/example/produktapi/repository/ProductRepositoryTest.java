package com.example.produktapi.repository;

import com.example.produktapi.model.Product;
import jdk.jfr.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

/*
    public ProductRepositoryTest(ProductRepository underTest) {
        this.underTest = underTest;
    }*/


    @Test
    void testingOurRepository() {
        List<Product> products = underTest.findAll();
        Assertions.assertFalse(products.isEmpty());
    }


    void findByTitle() {
    }
    //Mock = istället för att man skapar ett actuall product repository så skapar den en dummy, så tanken är att man kan modifiera en repository för testsyften
    //@mock så man kan

}