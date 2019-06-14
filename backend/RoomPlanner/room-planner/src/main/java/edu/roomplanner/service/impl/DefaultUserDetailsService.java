package edu.roomplanner.service.impl;

import edu.roomplanner.entity.RoleEntity;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userDetailsService")
public class DefaultUserDetailsService implements UserDetailsService {

    private UserRepository users;

    @Autowired
    public DefaultUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email: " + email + " not found"));
        return buildCustomerUserDetails(userEntity);
    }

    private UserDetails buildCustomerUserDetails(UserEntity userEntity) {
        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(transformEntityRolesToStringRoles(userEntity.getRoles()))
                .build();
    }

    private List<SimpleGrantedAuthority> transformEntityRolesToStringRoles(List<RoleEntity> roleEntities) {
        List<SimpleGrantedAuthority> result = new ArrayList<>();
        for(RoleEntity roleEntity:roleEntities) {
            result.add(new SimpleGrantedAuthority(roleEntity.getName()));
        }
        return result;
    }

}