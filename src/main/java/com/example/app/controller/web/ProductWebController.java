package com.example.app.controller.web;

import com.example.app.dto.ProductDTO;
import com.example.app.service.ProductService;
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
@RequestMapping("/products")
public class ProductWebController {

    private final ProductService productService;

    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<ProductDTO> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("mode", "idle");
        return "products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("mode", "adding");
        model.addAttribute("pageTitle", "Add Product");
        return "products/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProductDTO> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            model.addAttribute("mode", "editing");
            model.addAttribute("pageTitle", "Edit Product");
            return "products/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            return "redirect:/products";
        }
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProductDTO> productOpt = productService.findById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            model.addAttribute("mode", "deleting");
            model.addAttribute("pageTitle", "Delete Product");
            return "products/delete";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            return "redirect:/products";
        }
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                              BindingResult bindingResult,
                              @RequestParam String mode,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", mode);
            model.addAttribute("pageTitle", "adding".equals(mode) ? "Add Product" : "Edit Product");
            return "products/form";
        }

        try {
            if ("adding".equals(mode)) {
                productService.create(productDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Product created successfully");
            } else if ("editing".equals(mode)) {
                productService.update(productDTO.getId(), productDTO);
                redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully");
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("mode", mode);
            model.addAttribute("pageTitle", "adding".equals(mode) ? "Add Product" : "Edit Product");
            return "products/form";
        }

        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }
}