package edu.roomplanner.service.impl;

import edu.roomplanner.service.TokenParserService;
import edu.roomplanner.util.AuthenticationHelperUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenParserServiceImpl implements TokenParserService {

    @Override
    public String getEmailFromToken() {
        Authentication authentication = AuthenticationHelperUtil.getAuthentication();
        Object email = authentication.getPrincipal();
        if (email instanceof UserDetails)
            return ((UserDetails) email).getUsername();
        return (String) email;
    }
}