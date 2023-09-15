package com.owl.aipartner.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.owl.aipartner.converter.UserConverter;
import com.owl.aipartner.exception.NotFoundException;
import com.owl.aipartner.exception.UnprocessableEntityException;
import com.owl.aipartner.model.dto.UserRequest;
import com.owl.aipartner.model.dto.UserResponse;
import com.owl.aipartner.model.dto.UserQueryParameter;
import com.owl.aipartner.model.po.User;
import com.owl.aipartner.repository.mongo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("無法找到使用者資料"));
    }

    public UserResponse getUserResponse(String id) {
        return UserConverter.toUserResponse(getUser(id));
    }

    public List<UserResponse> getUsersResponse(UserQueryParameter params) {
        List<User> users = userRepository.findByAgeBetweenAndNameLikeIgnoreCase(
                Optional.ofNullable(params.getAgeFrom()).orElse(0),
                Optional.ofNullable(params.getAgeTo()).orElse(100),
                Optional.ofNullable(params.getName()).orElse(""),
                genSort(params.getOrderBy(), params.getSortBy()));
        return users.stream()
                .map(UserConverter::toUserResponse)
                .collect(Collectors.toList());
    }

    private Sort genSort(String orderBy, String sortBy) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortBy)) {
            Sort.Direction direction = Sort.Direction.fromString(sortBy);
            sort = Sort.by(direction, orderBy);
        }

        return sort;
    }

    public UserResponse createUser(UserRequest request) {
        User user = UserConverter.toUser(request);

        return UserConverter.toUserResponse(userRepository.insert(user));
    }

    public UserResponse updateUser(String id, UserRequest request) {
        User oldUser = getUser(id);
        User newUser = UserConverter.toUser(request);
        newUser.setId(oldUser.getId());

        return UserConverter.toUserResponse(userRepository.save(newUser));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
