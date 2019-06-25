package edu.roomplanner.service.impl;

import edu.roomplanner.service.AuthenticationHelperService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationHelperServiceImpl implements AuthenticationHelperService {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}