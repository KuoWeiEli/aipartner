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

import com.owl.aipartner.model.converter.UserConverter;
import com.owl.aipartner.model.dto.UserDTO;
import com.owl.aipartner.model.dto.UserQueryParameter;
import com.owl.aipartner.model.po.UserPO;
import com.owl.aipartner.service.UserService;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") String id) {
        UserPO userPO = userService.getUser(id);
        return ResponseEntity.ok().body(UserConverter.getUserDTO(userPO));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findUser(@ModelAttribute UserQueryParameter params) {
        List<UserPO> data = userService.getUsers(params);

        return ResponseEntity.ok().body(UserConverter.getUserDTOList(data));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO request) {
        UserPO userPO = userService.createUser(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userPO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(UserConverter.getUserDTO(userPO));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO user) {
        UserPO userPO = userService.updateUser(id, user);
        return ResponseEntity.ok(UserConverter.getUserDTO(userPO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
