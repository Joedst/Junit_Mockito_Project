package com.example.produktapi.service;

import com.example.produktapi.exception.BadRequestException;
import com.example.produktapi.exception.EntityNotFoundException;
import com.example.produktapi.model.Product;
import com.example.produktapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {  //Integer number                           //Successfully Tested!
       /* if(number == 2){
            productRepository.findAll();
        } */
        return productRepository.findAll();
    }

    public List<String> getAllCategories() {                                            //Successfully tested!
        return productRepository.findAllCategories();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product getProductById(Integer id) {

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new EntityNotFoundException(id);
        }
        return product.get();
    }

    public Product addProduct(Product product) {

        Optional<Product> p = productRepository.findByTitle(product.getTitle());
        if (p.isPresent()) {
            throw new BadRequestException("En produkt med titeln: "+ product.getTitle() + " finns redan");
        }
        return productRepository.save(product);
    }    //Successfully tested!!

    public Product updateProduct(Product updatedProduct, Integer id) {

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
             throw new EntityNotFoundException(id);
        }
        return productRepository.save(product.get());
    }

    public void deleteProduct(Integer id) {

        Optional product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new EntityNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
