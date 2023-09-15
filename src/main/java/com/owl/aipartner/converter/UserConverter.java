package com.owl.aipartner.converter;

import com.owl.aipartner.model.dto.UserRequest;
import com.owl.aipartner.model.dto.UserResponse;
import com.owl.aipartner.model.po.User;

public class UserConverter {
    private UserConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static User toUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        return user;
    }

    public static UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setAge(user.getAge());
        return response;
    }
}
