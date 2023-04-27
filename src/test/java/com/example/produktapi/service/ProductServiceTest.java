package com.example.produktapi.service;

import com.example.produktapi.exception.BadRequestException;
import com.example.produktapi.exception.EntityNotFoundException;
import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.BDDMockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @InjectMocks
    private ProductService underTest;


    @Test
    void whenSearchingForAllProducts_thenReturnAllProducts() {
        //given
        List<Product> products = Arrays.asList(
                new Product("En dator", 25000.0, "elektronik", "Bra att ha", "datorBild"),
                new Product("Diamanthalsband", 2560.0, "jewelry", "Extra stamina irl", "diamantBild"));
        ProductRepository mockedRepository = mock(ProductRepository.class);
        when(mockedRepository.findAll()).thenReturn(products);
        //when
        List<Product> allProducts = mockedRepository.findAll();
        //then
        assertEquals(2, allProducts.size());

    }


    @Test
    void whenSearchingForAllCategories_thenReturnAllCategories() {
        List<String> categoryList = new ArrayList<String>();
        categoryList.add("Elektronik");
        categoryList.add("Hushållsvaror");
        //My mock repo
        ProductRepository mockedRepository = mock(ProductRepository.class);
        //What my repo is doing - When findAllCategories is called on, så ska den returna category list
        when(mockedRepository.findAllCategories()).thenReturn(categoryList);
        //Result
        List<String> result = mockedRepository.findAllCategories();
        assertEquals(2, result.size());
        assertEquals(categoryList, result);
    }

    @Test
    void whenSearchingForProductInCategory_thenGetCorrectProduct() {
        //given
        String category = "elektronik";
        Product product = new Product("fotboll", 34.0, "elektronik", "", "");
        ProductRepository mockedRepository = mock(ProductRepository.class);
        when(mockedRepository.findByCategory(category)).thenReturn(Collections.singletonList(product));
        //when
        List<Product> products = mockedRepository.findByCategory(category);
        //then
        assertEquals(1, products.size());
        assertEquals("fotboll", products.get(0).getTitle());
        assertEquals("fotboll", product.getTitle());
    }


    @Test
    void whenSearchingForACategory_thenGetCorrectCategory() {
        List<String> categoryList = new ArrayList<String>();
        categoryList.add("Elektronik");
        categoryList.add("Hushållsvaror");
        assertEquals("Elektronik", categoryList.get(0));
    }

    //public List<Product> getAllProducts() {
    //        return productRepository.findAll();
    //    } som finns i ProductService.Java är det som ska testas
    @Test
    void givenGetAllProducts_thenExactlyOneInteractionWithRepoMethodFindAll() {
        //when
        underTest.getAllProducts(); //när vi kör den här
        //then
        verify(repository, times(1)).findAll(); //så veirfierar vi att metoden ropas på med find all
        verifyNoMoreInteractions(repository);
    }


    @Test
    void whenGetAllCategories_thenExactlyOneInteractionGetByCategory() {
        //when
        underTest.getAllCategories();
        //then
        verify(repository, times(1)).findAllCategories();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenAnExistingCategory_whenGetProductsByCategory_thenReceivesANonEmptyList() {
        //given
        List<Product> productList = Arrays.asList(
                new Product("Product1", 100.0, "Electronics", "", ""),
                new Product("Product2", 50.0, "Electronics", "", "")
        );
        when(repository.findByCategory("Electronics")).thenReturn(productList);
        //when
        List<Product> productsByCategory = underTest.getProductsByCategory("Electronics");
        assertFalse(productsByCategory.isEmpty());
    }

    @Test
    void whenAddingAProduct_thenSaveMethodShouldBeCalled() {
        //given
        Product product = new Product("Rätt objekt som sparas", 4000.0, "", "", "");
        //when
        underTest.addProduct(product);
        //then
        verify(repository).save(productCaptor.capture());
        assertEquals(product, productCaptor.getValue());

    }

    @Test
    void whenUpdatingProductWithNewId_thenHaveNewIdShow() {
//given
        Integer id = 1;
        Product productBefore = new Product("produktBeforeUpdate", 34.0, "", "", "");
        productBefore.setId(id);
        Product productAfter = new Product("produktAfterUpdate", 1337.0, "", "", "");
        when(repository.findById(id)).thenReturn(Optional.of(productAfter));
        when(repository.save(productAfter)).thenReturn(productAfter);
        Product updatedResult = underTest.updateProduct(productAfter, id);
        assertEquals(updatedResult, productAfter);

    }


    @Test
    void whenUpdatingProductWithExistingId_thenThrowError() {            //updateProduct (felflöde)
//given
        Integer id = 1;
        Product productBefore = new Product("produktBeforeUpdate", 34.0, "", "", "");
        productBefore.setId(id);
        Product productAfter = new Product("produktAfterUpdate", 1337.0, "", "", "");
        when(repository.findById(id)).thenReturn(Optional.of(productBefore));
        when(repository.save(productBefore)).thenThrow(new RuntimeException("Produkt med id" + id + "finns redan"));
        Exception exception = assertThrows(RuntimeException.class, () -> underTest.updateProduct(productAfter, id));
        assertEquals("Produkt med id" + id + "finns redan", exception.getMessage());

    }


    @Test
    void givenProductId_whenDeleteProduct_thenProductIsSuccessfullyDeleted() {
        Integer id = 1;
        Product product = new Product("produkt", 34.0, "", "", "");
        product.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(product));
        underTest.deleteProduct(id);
        verify(repository, times(1)).deleteById(id);

    }


    @Test
    void givenNonexistentId_whenDeleteProduct_thenThrowError() {  //deleteProduct (felflöde)
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            underTest.deleteProduct(id);
        });
        assertEquals("Produkt med id " + id + " hittades inte", exception.getMessage());
        verify(repository, never()).deleteById(any());
        verifyNoMoreInteractions(repository);
    }


    @Test
    void whenAddingProductWithExistingTitle_thenThrowError() {
        //given
        String title = "vår test-titel";
        Product product = new Product(title, 34.0, "", "", "");
        given(repository.findByTitle(title)).willReturn(Optional.of(product));
        //Så länge den här metoden anropas på med vår test titel så ska den returnera produkt
        //when,then
        assertThrows(BadRequestException.class, () -> underTest.addProduct(product),
                "En produkt med titeln '" + title + "' finns redan");
        verify(repository, times(1)).findByTitle(title); //verifierar att den anropats bara
        verify(repository, never()).save(any());

    }

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;

    @Test
    void whenGettingProductById_thenReturnProductWithCorrectId() {                  // - getProductById (normalflöde)
        // Given
        Product product = new Product("Laptop", 2000.0, "Electronics", "", "");
        product.setId(4);
        // When
        repository.save(product);
        // Then
        assertEquals(product.getId(), 4);
    }


    @Test
    void whenGettingProductById_thenThrowErrorIfIdMismatch() {               // - getProductById(felflöde)
        // Given
        int actualId = 5;

        Product product = new Product("Laptop", 2000.0, "Electronics", "", "");
        product.setId(actualId);
        // When
        when(repository.findById(actualId)).thenReturn(Optional.empty());
        repository.save(product);
        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> underTest.getProductById(actualId));
        assertEquals("Produkt med id " +actualId+ " hittades inte", exception.getMessage());
    }


    @Test
    void githubActionsTestThatShouldFail() {
        Assertions.assertEquals(10, 10);

    }


}




