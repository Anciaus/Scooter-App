package com.tieto.scooter.services;

import com.tieto.scooter.models.StationViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tieto.scooter.utils.Dto.setup;
import static java.util.Arrays.asList;

@Service
public class StationService {

    public List<StationViewModel> getStations() {
        return new ArrayList<>(asList(
                setup(new StationViewModel(), station -> {
                    station.name = "Andromeda";
                    station.parkedScooters = 2;
                }),
                setup(new StationViewModel(), station -> {
                    station.name = "MilkyWay";
                    station.parkedScooters = 1;
                })
        ));
    }
}
