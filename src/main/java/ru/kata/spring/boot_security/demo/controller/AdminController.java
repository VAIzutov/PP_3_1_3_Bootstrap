package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User currentUser = userService.findByUsername(email);
        model.addAttribute("user", currentUser);

        model.addAttribute("users", userService.getAllUsers());

        model.addAttribute("allRoles", roleService.getAllRoles());

        model.addAttribute("newUser", new User());

        return "admin";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") User user) {
        Set<Role> roles = roleService.findByIds(user.getRoleIds());
        user.setRoles(roles);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") User user) {
        Set<Role> roles = roleService.findByIds(user.getRoleIds());
        user.setRoles(roles);
        user.setId(id);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}