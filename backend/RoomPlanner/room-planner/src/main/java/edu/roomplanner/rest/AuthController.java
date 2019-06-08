package edu.roomplanner.rest;

import edu.roomplanner.dto.CustomUserDetails;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.repository.PersonRepository;
import edu.roomplanner.security.AuthenticationRequest;
import edu.roomplanner.service.impl.CustomUserDetailsService;
import edu.roomplanner.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PersonRepository persons;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        try {
            String email = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword()));
            PersonEntity personEntity = this.persons.findByEmail(email).
                    orElseThrow(() -> new UsernameNotFoundException("Email " + email + "not found"));
            CustomUserDetails customUserDetails = customUserDetailsService.buildCustomerUserDetails(personEntity);
            String token = jwtTokenProvider.createToken(email, customUserDetails.getRoles());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", email);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}