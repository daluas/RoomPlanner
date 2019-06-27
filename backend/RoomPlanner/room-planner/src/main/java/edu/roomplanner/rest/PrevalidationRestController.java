package edu.roomplanner.rest;

import edu.roomplanner.service.PrevalidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
public class PrevalidationRestController {


    private PrevalidationService prevalidationService;

    @Autowired
    public PrevalidationRestController(PrevalidationService prevalidationService) {
        this.prevalidationService = prevalidationService;
    }

    private static final Logger LOGGER = LogManager.getLogger(PrevalidationRestController.class);

    @RequestMapping(method = RequestMethod.GET, value = "api/prevalidation", produces = "application/json")
    @PreAuthorize("hasAuthority('person')")
    public ResponseEntity<HttpStatus> prevalidation(@RequestParam("roomId") Long roomId, @RequestParam @DateTimeFormat(pattern = "EEE',' dd MMM yyyy HH:mm:ss 'GMT'") Calendar startDate,
                                                    @RequestParam @DateTimeFormat(pattern = "EEE',' dd MMM yyyy HH:mm:ss 'GMT'") Calendar endDate, @RequestParam("email") String email) {

        LOGGER.info("Method was called.");
        Integer result = prevalidationService.prevalidate(startDate, endDate, email, roomId);
        LOGGER.info("The following object was returned:" + result);
        if (result == 200) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
