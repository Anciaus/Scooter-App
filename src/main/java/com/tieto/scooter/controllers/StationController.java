package com.tieto.scooter.controllers;

import com.tieto.scooter.models.StationViewModel;
import com.tieto.scooter.services.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/station")
public class StationController {

    @Autowired
    StationService stationService;

    @GetMapping("/stations")
    public List<StationViewModel> stations() {
        return stationService.getStations();
    }
}
