package edu.roomplanner.rest;

import edu.roomplanner.dto.FloorDto;
import edu.roomplanner.dto.PersonDto;
import edu.roomplanner.service.FloorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FloorRestController {

    private FloorService floorService;

    private static final Logger LOGGER = LogManager.getLogger(FloorRestController.class);

    @Autowired
    public FloorRestController(FloorService floorService) {
        this.floorService = floorService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/floors")
    @ApiOperation("Gets a list of all floors")
    @ApiResponses(value = {@ApiResponse(code = 301, message = "The following floors were returned."),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    @PreAuthorize("hasAuthority('person')")
    ResponseEntity<List<FloorDto>> getAllFloors() {

        LOGGER.info("Method was called.");
        List<FloorDto> allFloors = floorService.getAllFloors();
        LOGGER.info("The following object was returned:" + allFloors);
        return new ResponseEntity<>(allFloors, HttpStatus.FOUND);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/floors/{floor}")
    @ApiOperation("Gets a floor with an specific \"name\"")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "This floor was not found."),
            @ApiResponse(code = 301, message = "The following floor was found", response = FloorDto.class),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 500, message = "Internal server error.")})
    @PreAuthorize("hasAuthority('person')")
    ResponseEntity<FloorDto>getFloorByName(@PathVariable Integer floor){
        FloorDto floorDto = floorService.getFloorByFloor(floor);

        if(floorDto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(floorDto,HttpStatus.FOUND);
    }

}