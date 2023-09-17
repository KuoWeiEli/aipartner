package com.owl.aipartner.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.owl.aipartner.exception.NotFoundException;
import com.owl.aipartner.model.user.AppUser;
import com.owl.aipartner.service.UserService;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            AppUser user = userService.findByEmail(username);
            return new MyUserDetails(user);
        } catch (NotFoundException notFoundException) {
            throw new UsernameNotFoundException("Wrong username!");
        }
    }
}
