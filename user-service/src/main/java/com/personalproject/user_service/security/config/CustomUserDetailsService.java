package com.personalproject.user_service.security.config;


import com.personalproject.user_service.models.Account;
import com.personalproject.user_service.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("no user found");
                    return new UsernameNotFoundException("User not found");
                });
        System.out.println(user);
//        System.out.println(user.getUserType());

        return new CustomUserDetails(user, List.of(new SimpleGrantedAuthority(user.getType().toString().toLowerCase())));
    }
}
