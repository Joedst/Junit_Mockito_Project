package com.example.produktapi.repository;

import com.example.produktapi.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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


    @Test
    void whenSearchingForAllProducts_thenReturnAllProducts() {
        //given

        List<Product> products = Arrays.asList(
                new Product("En dator", 25000.0, "elektronik", "Bra att ha", "diamantBild"),
                new Product("Diamanthalsband", 2560.0, "jewelry", "Extra stamina irl", "diamantBild"));


        ProductRepository mockedRepository = mock(ProductRepository.class);
        when(mockedRepository.findAll()).thenReturn(products);


//when
        List<Product> allProducts = mockedRepository.findAll();


        //then
assertEquals(2,allProducts.size());


    }




    @Test
    void whenSearchingForAnExistingTitle_thenReturnThatProduct() {
        //given
        String title = "En dator";
        Product product = new Product(title,    //underTest.save
                25000.0,
                "elektronik",
                "Bra att ha"
                , "urlTillBild");
        underTest.save(product);
//when
        Optional<Product> optionalProduct = underTest.findByTitle(title);
        //then
        Assertions.assertTrue(optionalProduct.isPresent());
        Assertions.assertFalse(optionalProduct.isEmpty());
        Assertions.assertEquals(title, optionalProduct.get().getTitle(), "En dator test complete");
        Assertions.assertAll(
                () -> assertTrue(optionalProduct.isPresent()),
                () -> assertFalse(optionalProduct.isEmpty()),
                () -> assertEquals(title, optionalProduct.get()));

    }

    @Test
    void whenSearchingForNonExistingTitle_then() {
        //tiven
        String title = "Titel som inte finns";

       /*
    Product product = new Product(title,    //underTest.save
            25000.0,
            "elektronik",
            "Bra att ha"
            , "urlTillBild");

    underTest.save(product); */
        //when
        Optional<Product> optionalProduct = underTest.findByTitle(title);
        //then
        Assertions.assertAll(
                () -> assertTrue(optionalProduct.isEmpty(), "isEmpty = true"),
                () -> assertTrue(optionalProduct.isPresent(), "isPresent = false"),
                () -> assertThrows(NoSuchElementException.class, () -> optionalProduct.get())
        );
    }


    void findByTitle() {
    }
    //Mock = istället för att man skapar ett actuall product repository så skapar den en dummy, så tanken är att man kan modifiera en repository för testsyften
    //@mock så man kan

}