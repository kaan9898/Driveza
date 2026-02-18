package com.team3.driveza.controller;

import com.team3.driveza.Dto.User.UserDetailDto;
import com.team3.driveza.Dto.User.UserFormDto;
import com.team3.driveza.Dto.User.UserListDto;
import com.team3.driveza.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * User account management pages supported by Thymeleaf views.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Display the list page with all users.
    @GetMapping
    public String listUsers(Model model) {
        List<UserListDto> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    // Render an empty form for creating a user.
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserFormDto());
        model.addAttribute("formAction", "/users");
        return "users/form";
    }

    // Persist the form submission and redirect to the list.
    @PostMapping
    public String createUser(@ModelAttribute("user") UserFormDto userForm) {
        userService.createUser(userForm);
        return "redirect:/users";
    }

    // Show read-only details for one user.
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        UserDetailDto user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/detail";
    }

    // Populate the shared form for editing an existing user.
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserDetailDto detail = userService.getUserById(id);
        UserFormDto form = toForm(detail);
        model.addAttribute("user", form);
        model.addAttribute("formAction", "/users/" + id + "/edit");
        return "users/form";
    }

    // Accept the edit form submission and return to the list.
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") UserFormDto userForm) {
        userService.updateUser(id, userForm);
        return "redirect:/users";
    }

    // Delete action triggered by the detail/form views.
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    private UserFormDto toForm(UserDetailDto detail) {
        UserFormDto form = new UserFormDto();
        form.setId(detail.getId());
        form.setName(detail.getName());
        form.setEmail(detail.getEmail());
        form.setRole(detail.getRole());
        form.setDob(detail.getDob());
        return form;
    }
}
