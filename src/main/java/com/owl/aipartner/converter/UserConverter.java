package com.owl.aipartner.converter;

import com.owl.aipartner.model.user.AppUser;
import com.owl.aipartner.model.user.AppUserRequest;
import com.owl.aipartner.model.user.AppUserResponse;

public class UserConverter {
    private UserConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static AppUser toUser(AppUserRequest request) {
        AppUser user = new AppUser();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setUserAuthorities(request.getAuthorities());
        return user;
    }

    public static AppUserResponse toUserResponse(AppUser user) {
        AppUserResponse response = new AppUserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setAge(user.getAge());
        response.setEmail(user.getEmail());
        response.setUserAuthorities(user.getUserAuthorities());
        return response;
    }
}
