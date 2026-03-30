package com.example.app.service;

import com.example.app.dto.TransactionDTO;
import com.example.app.entity.Customer;
import com.example.app.entity.Product;
import com.example.app.entity.Transaction;
import com.example.app.repository.CustomerRepository;
import com.example.app.repository.ProductRepository;
import com.example.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository,
                              ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<TransactionDTO> findAll() {
        return transactionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<TransactionDTO> findById(Long id) {
        return transactionRepository.findById(id).map(this::toDTO);
    }

    public TransactionDTO create(TransactionDTO dto) {
        if (transactionRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("Transaction with ID " + dto.getId() + " already exists");
        }
        validateTransaction(dto);
        
        BigDecimal calculatedPrice = calculatePrice(dto.getProdId(), dto.getQuantity());
        dto.setPrice(calculatedPrice);
        
        if (dto.getTDate() == null) {
            dto.setTDate(LocalDate.now());
        }
        
        Transaction transaction = toEntity(dto);
        Transaction saved = transactionRepository.save(transaction);
        return toDTO(saved);
    }

    public TransactionDTO update(Long id, TransactionDTO dto) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction with ID " + id + " not found");
        }
        validateTransaction(dto);
        
        BigDecimal calculatedPrice = calculatePrice(dto.getProdId(), dto.getQuantity());
        dto.setPrice(calculatedPrice);
        
        dto.setId(id);
        Transaction transaction = toEntity(dto);
        Transaction saved = transactionRepository.save(transaction);
        return toDTO(saved);
    }

    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction with ID " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }

    public BigDecimal calculatePrice(Long productId, Integer quantity) {
        if (productId == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            BigDecimal productPrice = productOpt.get().getPrice();
            return productPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    private void validateTransaction(TransactionDTO dto) {
        if (dto.getCustId() == null || dto.getCustId() <= 0) {
            throw new IllegalArgumentException("Valid Customer ID is required");
        }
        if (!customerRepository.existsById(dto.getCustId())) {
            throw new IllegalArgumentException("Customer with ID " + dto.getCustId() + " not found");
        }
        if (dto.getProdId() == null || dto.getProdId() <= 0) {
            throw new IllegalArgumentException("Valid Product ID is required");
        }
        if (!productRepository.existsById(dto.getProdId())) {
            throw new IllegalArgumentException("Product with ID " + dto.getProdId() + " not found");
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Valid quantity is required");
        }
    }

    private TransactionDTO toDTO(Transaction entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setCustId(entity.getCustId());
        dto.setProdId(entity.getProdId());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setTDate(entity.getTDate());
        
        Optional<Customer> customerOpt = customerRepository.findById(entity.getCustId());
        customerOpt.ifPresent(customer -> dto.setCustName(customer.getName()));
        
        Optional<Product> productOpt = productRepository.findById(entity.getProdId());
        productOpt.ifPresent(product -> dto.setProdName(product.getName()));
        
        return dto;
    }

    private Transaction toEntity(TransactionDTO dto) {
        Transaction entity = new Transaction();
        entity.setId(dto.getId());
        entity.setCustId(dto.getCustId());
        entity.setProdId(dto.getProdId());
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        entity.setTDate(dto.getTDate());
        return entity;
    }
}