package com.example.app.controller.web;

import com.example.app.dto.CustomerDTO;
import com.example.app.service.CustomerService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {

    private final CustomerService customerService;

    public CustomerWebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(Model model) {
        List<CustomerDTO> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("mode", "idle");
        return "customers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());
        model.addAttribute("mode", "adding");
        model.addAttribute("pageTitle", "Add Customer");
        return "customers/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CustomerDTO> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            model.addAttribute("mode", "editing");
            model.addAttribute("pageTitle", "Edit Customer");
            return "customers/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Customer not found");
            return "redirect:/customers";
        }
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CustomerDTO> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            model.addAttribute("mode", "deleting");
            model.addAttribute("pageTitle", "Delete Customer");
            return "customers/delete";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Customer not found");
            return "redirect:/customers";
        }
    }

    @PostMapping("/save")
    public String saveCustomer(@Valid @ModelAttribute("customer") CustomerDTO customerDTO,
                               BindingResult bindingResult,
                               @RequestParam String mode,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", mode);
            model.addAttribute("pageTitle", "adding".equals(mode) ? "Add Customer" : "Edit Customer");
            return "customers/form";
        }

        try {
            if ("adding".equals(mode)) {
                customerService.create(customerDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Customer created successfully");
            } else if ("editing".equals(mode)) {
                customerService.update(customerDTO.getId(), customerDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("mode", mode);
            model.addAttribute("pageTitle", "adding".equals(mode) ? "Add Customer" : "Edit Customer");
            return "customers/form";
        }

        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customers";
    }
}