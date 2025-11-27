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

import java.util.HashSet;
import java.util.List;
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
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam List<Long> roles) {
        Set<Role> roleObjects = roleService.findByIds(new HashSet<>(roles));
        user.setRoles(roleObjects);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam Integer age,
                             @RequestParam String username,
                             @RequestParam(required = false) String password,
                             @RequestParam List<Long> roles) {
        User existingUser = userService.getUserById(id);
        existingUser.setName(name);
        existingUser.setSurname(surname);
        existingUser.setAge(age);
        existingUser.setUsername(username);
        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(password);
        }
        Set<Role> roleObjects = roleService.findByIds(new HashSet<>(roles));
        existingUser.setRoles(roleObjects);
        userService.saveUser(existingUser);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}