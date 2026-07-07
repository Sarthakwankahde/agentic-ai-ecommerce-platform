package com.sarthak.agenticai.security;

import com.sarthak.agenticai.entity.User;
import com.sarthak.agenticai.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

}
