package edu.roomplanner.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class BasicController {

    @GetMapping
    public String test() {return "ok";}

    @GetMapping("/person")
    @PreAuthorize("hasAuthority('person')")
    public String person() {return "person";}

    @GetMapping("/room")
    @PreAuthorize("hasAuthority('room')")
    public String room() {return "room";}
}
