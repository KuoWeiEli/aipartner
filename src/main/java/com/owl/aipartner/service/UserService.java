package com.owl.aipartner.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.owl.aipartner.exception.NotFoundException;
import com.owl.aipartner.exception.UnprocessableEntityException;
import com.owl.aipartner.model.User;
import com.owl.aipartner.model.UserQueryParameter;
import com.owl.aipartner.repository.mongo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("無法找到使用者資料"));
    }

    public List<User> getUsers(UserQueryParameter params) {
        return userRepository.findByAgeBetweenAndNameLikeIgnoreCase(
                Optional.ofNullable(params.getAgeFrom()).orElse(0),
                Optional.ofNullable(params.getAgeTo()).orElse(100),
                Optional.ofNullable(params.getName()).orElse(""),
                genSort(params.getOrderBy(), params.getSortBy()));
    }

    private Sort genSort(String orderBy, String sortBy) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortBy)) {
            Sort.Direction direction = Sort.Direction.fromString(sortBy);
            sort = Sort.by(direction, orderBy);
        }

        return sort;
    }

    public User createUser(User request) {
        Optional<User> data = userRepository.findById(request.getId());
        if (data.isPresent())
            throw new UnprocessableEntityException("The id of product is duplicated!");

        User user = new User(request.getId(), request.getName(), request.getAge());
        return userRepository.insert(user);
    }

    public User updateUser(Long id, User request) {
        User user = getUser(id);

        user.setName(request.getName());
        user.setAge(request.getAge());

        return userRepository.save(user);
    }

    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
