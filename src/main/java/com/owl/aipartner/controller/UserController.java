package com.owl.aipartner.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.owl.aipartner.model.user.UserQueryParameter;
import com.owl.aipartner.model.user.AppUserRequest;
import com.owl.aipartner.model.user.AppUserResponse;
import com.owl.aipartner.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(userService.getUserResponse(id));
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> findUser(@ModelAttribute UserQueryParameter params) {
        return ResponseEntity.ok().body(userService.getUsersResponse(params));
    }

    @PostMapping
    public ResponseEntity<AppUserResponse> createUser(@Valid @RequestBody AppUserRequest request) {
        AppUserResponse response = userService.createUser(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<AppUserResponse> updateUser(@PathVariable String id, @Valid @RequestBody AppUserRequest user) {
        AppUserResponse response = userService.updateUser(id, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AppUserResponse> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
