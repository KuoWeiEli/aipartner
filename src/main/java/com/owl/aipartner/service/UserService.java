package com.owl.aipartner.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.owl.aipartner.exception.NotFoundException;
import com.owl.aipartner.exception.UnprocessableEntityException;
import com.owl.aipartner.model.User;
import com.owl.aipartner.model.UserQueryParameter;
import com.owl.aipartner.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("無法找到使用者資料"));
    }

    public List<User> getUsers(UserQueryParameter params) {
        List<User> data = userRepository.findByNameIgnoreCaseLike("%" + params.getName() + "%");
        data.sort(genSortComparator(params.getOrderBy(), params.getSortBy()));

        return data;
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

    public User createUser(User request) {
        Optional<User> data = userRepository.findById(request.getUserid());
        if (data.isPresent())
            throw new UnprocessableEntityException("The id of product is duplicated!");

        return new User(request.getUserid(), request.getName(), request.getAge());
    }

    public User updateUser(Long id, User request) {
        User user = getUser(id);

        user.setName(request.getName());
        user.setAge(request.getAge());

        return userRepository.save(user);
    }

    public void deleteUser(@PathVariable Long id) {
        User user = getUser(id);
        userRepository.deleteById(user.getUserid());
    }
}
