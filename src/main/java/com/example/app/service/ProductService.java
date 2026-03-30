package com.example.app.service;

import com.example.app.dto.ProductDTO;
import com.example.app.entity.Product;
import com.example.app.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id).map(this::toDTO);
    }

    public ProductDTO create(ProductDTO dto) {
        if (productRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("Product with ID " + dto.getId() + " already exists");
        }
        validateProduct(dto);
        Product product = toEntity(dto);
        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        validateProduct(dto);
        dto.setId(id);
        Product product = toEntity(dto);
        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    public BigDecimal getPriceById(Long id) {
        return productRepository.findById(id)
                .map(Product::getPrice)
                .orElse(BigDecimal.ZERO);
    }

    private void validateProduct(ProductDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Product description is required");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valid price is required");
        }
    }

    private ProductDTO toDTO(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        return dto;
    }

    private Product toEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        return entity;
    }
}