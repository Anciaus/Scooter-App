package lt.tieto.scooter.controllers;

import lt.tieto.scooter.dtos.StationDto;
import lt.tieto.scooter.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/station")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/stations")
    public List<StationDto> stations() {
        return stationService.getStations();
    }
}
