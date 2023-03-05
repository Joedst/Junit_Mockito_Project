package com.example.produktapi.service;

import com.example.produktapi.exception.BadRequestException;
import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.BDDMockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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


        ProductRepository mockedRepository = mock(ProductRepository.class);
        when(mockedRepository.findAllCategories()).thenReturn(categoryList);
        assertEquals(2, categoryList.size());

    }

    @Test
    void whenSearchingForProductInCategory_thenGetCorrectProduct(){
        //given
        Product product = new Product("fotboll", 34.0, "elektronik", "", "");


       // List<Product> productList = underTest.getProductsByCategory("elektronik");


assertEquals("fotboll",product.getTitle());


    }




@Test
void whenSearchingForACategory_thenGetCorrectCategory(){

    List<String> categoryList = new ArrayList<String>();
    categoryList.add("Elektronik");
    categoryList.add("Hushållsvaror");

    ProductRepository mockedRepository = mock(ProductRepository.class);




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
    void givenAnExistingCategory_whenGetProductsByCategory_thenRecievesANonEmptyList() {
        //when
        List<Product> productsByCategory = underTest.getProductsByCategory("Electronics");
        verify(repository, times(1));
    }

    @Test
    void whenAddingAProduct_thenSaveMethodShouldBeCalled() {
        //given
        Product product = new Product("Rätt objekt som sparas", 4000.0, "", "", "");
        //when
        underTest.addProduct(product);
        //then
        verify(repository).save(productCaptor.capture());
        assertEquals(product,productCaptor.getValue());

    }

    @Test
    void whenAddingProductWithExistingTitle_thenThrowError() {
        //given
        String title = "vår test-titel";
        Product product = new Product(title, 34.0, "", "", "");
        given(repository.findByTitle(title)).willReturn(Optional.of(product));
//Så länge den här metoden anropas på med vår test titel så ska den returnera produkt
        //when
        underTest.addProduct(product);
        //then
        assertThrows(BadRequestException.class,
                () -> underTest.addProduct(product));
        verify(repository, times(1)).findByTitle(title);//verifierar att den anropats bara
        verify(repository, never()).save(any());

        //assertEquals(product,"En produkt med titeln_ vår titel finns redan", Exception.getMessage());


    }


}
