package com.example.produktapi.repository;

import com.example.produktapi.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;


    @Test
    void testingOurRepository() {
        List<Product> products = underTest.findAll();
        Assertions.assertFalse(products.isEmpty());
    }

    @Test
    void whenFindingByCategory_thenReturnProductsInCategory() {
        // Given
        Product product1 = new Product("Laptop", 2000.0, "Electronics", "", "");
        Product product2 = new Product("Stavmixer", 400.0, "Home Appliances", "", "");
        underTest.save(product1);
        underTest.save(product2);
        // When
        List<Product> foundProducts = underTest.findByCategory("Electronics");
        // Then
        assertEquals(1, foundProducts.size());
        assertTrue(foundProducts.contains(product1));
    }


    @Test
    void whenFindingByTitle_thenReturnProductWithTitle() {
        // Given
        Product product = new Product("Laptop", 2000.0, "Electronics", "", "");
        underTest.save(product);
        // When
        Optional<Product> foundProduct = underTest.findByTitle("Laptop");
        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
    }




}