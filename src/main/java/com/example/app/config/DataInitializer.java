package com.example.app.config;

import com.example.app.entity.Customer;
import com.example.app.entity.Product;
import com.example.app.repository.CustomerRepository;
import com.example.app.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(CustomerRepository customerRepository,
                                       ProductRepository productRepository) {
        return args -> {
            if (customerRepository.count() == 0) {
                customerRepository.save(new Customer(1L, "John Doe", "123 Main St", "Apt 4B", 12345L, 5551234567L));
                customerRepository.save(new Customer(2L, "Jane Smith", "456 Oak Ave", "Suite 100", 67890L, 5559876543L));
                customerRepository.save(new Customer(3L, "Bob Johnson", "789 Pine Rd", "Building C", 11223L, 5555551234L));
            }

            if (productRepository.count() == 0) {
                productRepository.save(new Product(1L, "Whole Milk", "Fresh whole milk - 1 gallon", new BigDecimal("4.99")));
                productRepository.save(new Product(2L, "Skim Milk", "Fat-free skim milk - 1 gallon", new BigDecimal("4.49")));
                productRepository.save(new Product(3L, "Chocolate Milk", "Chocolate flavored milk - 1 quart", new BigDecimal("2.99")));
                productRepository.save(new Product(4L, "Heavy Cream", "Heavy whipping cream - 1 pint", new BigDecimal("3.99")));
            }
        };
    }
}