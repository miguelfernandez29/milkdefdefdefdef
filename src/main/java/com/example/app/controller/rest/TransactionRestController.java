package com.example.app.controller.rest;

import com.example.app.dto.TransactionDTO;
import com.example.app.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction", description = "Transaction Management API")
public class TransactionRestController {

    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(summary = "Get all transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.findAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO created = transactionService.create(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing transaction")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id,
                                                             @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO updated = transactionService.update(id, transactionDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/calculate-price")
    @Operation(summary = "Calculate price based on product and quantity")
    public ResponseEntity<BigDecimal> calculatePrice(@RequestParam Long productId,
                                                      @RequestParam Integer quantity) {
        BigDecimal price = transactionService.calculatePrice(productId, quantity);
        return ResponseEntity.ok(price);
    }
}