package edu.roomplanner.service.impl;

import edu.roomplanner.service.AuthenticationHelperService;
import edu.roomplanner.service.TokenParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenParserServiceImpl implements TokenParserService {

    private AuthenticationHelperService authenticationHelperService;

    @Autowired
    public TokenParserServiceImpl(AuthenticationHelperService authenticationHelperService) {
        this.authenticationHelperService = authenticationHelperService;
    }

    @Override
    public String getEmailFromToken() {
        Authentication authentication = authenticationHelperService.getAuthentication();
        Object email = authentication.getPrincipal();
        if (email instanceof UserDetails)
            return ((UserDetails) email).getUsername();
        return (String) email;
    }
}