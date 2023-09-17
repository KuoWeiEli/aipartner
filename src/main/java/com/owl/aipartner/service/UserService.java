package com.owl.aipartner.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.owl.aipartner.converter.UserConverter;
import com.owl.aipartner.exception.NotFoundException;
import com.owl.aipartner.exception.UnprocessableEntityException;
import com.owl.aipartner.model.user.AppUser;
import com.owl.aipartner.model.user.UserQueryParameter;
import com.owl.aipartner.model.user.AppUserRequest;
import com.owl.aipartner.model.user.AppUserResponse;
import com.owl.aipartner.repository.mongo.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("無法找到使用者資料"));
    }

    private AppUser getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("無法找到使用者資料"));
    }

    public AppUserResponse getUserResponse(String id) {
        return UserConverter.toUserResponse(getUser(id));
    }

    public List<AppUserResponse> getUsersResponse(UserQueryParameter params) {
        List<AppUser> users = userRepository.findByAgeBetweenAndNameLikeIgnoreCase(
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

    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent())
            throw new UnprocessableEntityException("Email 已被人使用！");

        AppUser user = UserConverter.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return UserConverter.toUserResponse(userRepository.insert(user));
    }

    public AppUserResponse updateUser(String id, AppUserRequest request) {
        AppUser oldUser = getUser(id);
        AppUser newUser = UserConverter.toUser(request);

        // 新密碼與舊密碼不同時，才需要進行設定
        if (!passwordEncoder.matches(oldUser.getPassword(), newUser.getPassword())) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        newUser.setId(oldUser.getId());
        return UserConverter.toUserResponse(userRepository.save(newUser));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
