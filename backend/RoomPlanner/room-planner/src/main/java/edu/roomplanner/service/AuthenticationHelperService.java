package edu.roomplanner.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationHelperService {
    Authentication getAuthentication();
}