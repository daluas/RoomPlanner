package edu.roomplanner.rest;

import edu.roomplanner.dto.UserDto;
import edu.roomplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserRestService {

    private final UserService userService;

    @Autowired
    public UserRestService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('person') or hasAuthority('room')")
    public ResponseEntity<UserDto> getUserEmailType(@RequestParam(name = "email") String email) {
        Optional<UserDto> userEmailTypeDtoOptional = userService.getUserDto(email);
        return userEmailTypeDtoOptional.
                map(userEmailTypeDto -> new ResponseEntity<>(userEmailTypeDto, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
