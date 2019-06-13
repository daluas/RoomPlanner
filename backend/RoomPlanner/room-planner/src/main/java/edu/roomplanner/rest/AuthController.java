package edu.roomplanner.rest;

import edu.roomplanner.dto.CustomUserDetails;
import edu.roomplanner.entity.PersonEntity;
import edu.roomplanner.dto.AuthenticationRequest;
import edu.roomplanner.entity.UserEntity;
import edu.roomplanner.repository.UserRepository;
import edu.roomplanner.service.impl.CustomUserDetailsService;
import edu.roomplanner.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository users;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserRepository users,
                          CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.users = users;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {
        try {
            String email = data.getEmail();
            String password = encryptPassword(data.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserEntity userEntity = this.users.findByEmail(email).
                    orElseThrow(() -> new UsernameNotFoundException("Email " + email + "not found"));
            CustomUserDetails customUserDetails = customUserDetailsService.buildCustomerUserDetails(userEntity);
            String token = jwtTokenProvider.createToken(email, customUserDetails.getAuthorities());
            Map<Object, Object> model = createSigninResponseModel(userEntity, token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    private String encryptPassword(String password) {
        return password;
    }

    private Map<Object, Object> createSigninResponseModel(UserEntity userEntity, String token) {
        Map<Object, Object> model = new HashMap<>();
        model.put("email", userEntity.getEmail());
        model.put("type", userEntity.getType());
        model.put("token", token);
        return model;
    }

}