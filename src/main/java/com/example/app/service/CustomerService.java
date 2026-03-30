package com.example.app.service;

import com.example.app.dto.CustomerDTO;
import com.example.app.entity.Customer;
import com.example.app.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> findById(Long id) {
        return customerRepository.findById(id).map(this::toDTO);
    }

    public CustomerDTO create(CustomerDTO dto) {
        if (customerRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("Customer with ID " + dto.getId() + " already exists");
        }
        validateCustomer(dto);
        Customer customer = toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return toDTO(saved);
    }

    public CustomerDTO update(Long id, CustomerDTO dto) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }
        validateCustomer(dto);
        dto.setId(id);
        Customer customer = toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return toDTO(saved);
    }

    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }
        customerRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return customerRepository.existsById(id);
    }

    private void validateCustomer(CustomerDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (dto.getAdd1() == null || dto.getAdd1().trim().isEmpty()) {
            throw new IllegalArgumentException("Address Line 1 is required");
        }
        if (dto.getAdd2() == null || dto.getAdd2().trim().isEmpty()) {
            throw new IllegalArgumentException("Address Line 2 is required");
        }
        if (dto.getPincode() == null || dto.getPincode() <= 0) {
            throw new IllegalArgumentException("Valid pincode is required");
        }
        if (dto.getPhone() == null || dto.getPhone() <= 0) {
            throw new IllegalArgumentException("Valid phone number is required");
        }
    }

    private CustomerDTO toDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAdd1(entity.getAdd1());
        dto.setAdd2(entity.getAdd2());
        dto.setPincode(entity.getPincode());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    private Customer toEntity(CustomerDTO dto) {
        Customer entity = new Customer();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAdd1(dto.getAdd1());
        entity.setAdd2(dto.getAdd2());
        entity.setPincode(dto.getPincode());
        entity.setPhone(dto.getPhone());
        return entity;
    }
}