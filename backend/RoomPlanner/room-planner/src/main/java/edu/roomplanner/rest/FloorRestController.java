package edu.roomplanner.rest;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.service.FloorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FloorRestController {

    private FloorService floorService;

    private static Logger LOGGER = LogManager.getLogger(UserRestController.class);

    @Autowired
    public FloorRestController(FloorService floorService) {
        this.floorService = floorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/floors")
    @PreAuthorize("hasAuthority('person')")
    ResponseEntity<List<FloorDto>> getAllFloors() {

        LOGGER.info("Method was called.");
        List<FloorDto> allFloors = floorService.getAllFloors();
        LOGGER.info("The following object was returned:" + allFloors);
        return new ResponseEntity<>(allFloors, HttpStatus.FOUND);
    }


}
