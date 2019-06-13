package edu.roomplanner.service.impl;

import edu.roomplanner.dto.CustomUserDetails;
import edu.roomplanner.entity.RoleEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("UserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email: " + email + " not found"));
        return buildCustomerUserDetails(userEntity);
    }

    public CustomUserDetails buildCustomerUserDetails(UserEntity userEntity) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return CustomUserDetails.builder()
                .email(userEntity.getEmail())
                .password(encoder.encode(userEntity.getPassword()))
                .roles(transformEntityRolesToStringRoles(userEntity.getRoles()))
                .build();
    }

    private List<String> transformEntityRolesToStringRoles(List<RoleEntity> roleEntities) {
        List<String> result = new ArrayList<>();
        for(RoleEntity roleEntity:roleEntities) {
            result.add(roleEntity.getName());
        }
        return result;
    }

}
