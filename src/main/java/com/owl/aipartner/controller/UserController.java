package com.owl.aipartner.controller;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.owl.aipartner.model.User;
import com.owl.aipartner.model.UserQueryParameter;
import com.owl.aipartner.repository.UserRepository;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        Optional<User> data = userRepository.findById(id);
        if (data.isPresent())
            return ResponseEntity.ok().body(data.get());
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> findUser(@ModelAttribute UserQueryParameter params) {
        List<User> data = userRepository.findByNameIgnoreCaseLike("%" + params.getName() + "%");
        data.sort(genSortComparator(params.getOrderBy(), params.getSortBy()));

        return ResponseEntity.ok().body(data);
    }

    private Comparator<User> genSortComparator(String orderBy, String sortBy) {
        Comparator<User> comparator = (p1, p2) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortBy)) {
            return comparator;
        }

        if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(User::getName);
        } else if (orderBy.equalsIgnoreCase("age")) {
            comparator = Comparator.comparing(User::getAge);
        }

        if ("desc".equalsIgnoreCase(sortBy)) {
            return comparator.reversed();
        }
        return comparator;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        Optional<User> data = userRepository.findById(user.getUserid());
        if (data.isPresent())
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();

        User inputUser = new User(user.getUserid(), user.getName(), user.getAge());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getUserid())
                .toUri();

        return ResponseEntity.created(uri).body(userRepository.save(inputUser));
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> data = userRepository.findById(id);
        if (data.isEmpty())
            return ResponseEntity.notFound().build();

        User userData = data.get();
        userData.setName(user.getName());
        userData.setAge(user.getAge());

        return ResponseEntity.ok(userRepository.save(userData));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
