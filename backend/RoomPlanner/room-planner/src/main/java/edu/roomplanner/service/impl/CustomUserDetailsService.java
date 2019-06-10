package edu.roomplanner.service.impl;

import edu.roomplanner.dto.CustomUserDetails;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.entity.RoleEntity;
import edu.roomplanner.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private PersonRepository persons;

    public CustomUserDetailsService(PersonRepository persons) {
        this.persons = persons;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PersonEntity personEntity = persons.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email: " + email + " not found"));
        return buildCustomerUserDetails(personEntity);
    }

    public CustomUserDetails buildCustomerUserDetails(PersonEntity personEntity) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return CustomUserDetails.builder()
                .email(personEntity.getEmail())
                .password(encoder.encode(personEntity.getPassword()))
                .roles(transformEntityRolesToStringRoles(personEntity.getRoleEntityList()))
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
