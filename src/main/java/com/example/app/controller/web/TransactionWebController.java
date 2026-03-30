package com.example.app.controller.web;

import com.example.app.dto.CustomerDTO;
import com.example.app.dto.ProductDTO;
import com.example.app.dto.TransactionDTO;
import com.example.app.service.CustomerService;
import com.example.app.service.ProductService;
import com.example.app.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionWebController {

    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final ProductService productService;

    public TransactionWebController(TransactionService transactionService,
                                    CustomerService customerService,
                                    ProductService productService) {
        this.transactionService = transactionService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public String listTransactions(Model model) {
        List<TransactionDTO> transactions = transactionService.findAll();
        model.addAttribute("transactions", transactions);
        model.addAttribute("mode", "idle");
        return "transactions/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setTDate(LocalDate.now());
        model.addAttribute("transaction", transaction);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("mode", "adding");
        model.addAttribute("pageTitle", "Add Transaction");
        return "transactions/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<TransactionDTO> transactionOpt = transactionService.findById(id);
        if (transactionOpt.isPresent()) {
            model.addAttribute("transaction", transactionOpt.get());
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("mode", "editing");
            model.addAttribute("pageTitle", "Edit Transaction");
            return "transactions/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction not found");
            return "redirect:/transactions";
        }
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<TransactionDTO> transactionOpt = transactionService.findById(id);
        if (transactionOpt.isPresent()) {
            model.addAttribute("transaction", transactionOpt.get());
            model.addAttribute("mode", "deleting");
            model.addAttribute("pageTitle", "Delete Transaction");
            return "transactions/delete";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaction not found");
            return "redirect:/transactions";
        }
    }

    @PostMapping("/save")
    public String saveTransaction(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO,
                                  BindingResult bindingResult,
                                  @RequestParam String mode,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("mode", mode);
            model.addAttribute("pageTitle", "adding".equals(mode) ? "Add Transaction" : "Edit Transaction");
            return "transactions/form";
        }

        try {
            if ("adding".equals(mode)) {
                transactionService.create(transactionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Transaction created successfully");
            } else if ("editing".equals(mode)) {
                transactionService.update(transactionDTO.getId(), transactionDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Transaction updated successfully");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("mode", mode);
            model.addAttribute("pageTitle", "adding".equals(mode) ? "Add Transaction" : "Edit Transaction");
            return "transactions/form";
        }

        return "redirect:/transactions";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transactionService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/transactions";
    }
}