package lt.tieto.scooter.services;

import lt.tieto.scooter.dtos.StationDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Service
public class StationService {

    public List<StationDto> getStations() {
        StationDto station1 = new StationDto();
        station1.name = "Andromeda";
        station1.parkedScooters = 2;

        StationDto station2 = new StationDto();
        station2.name = "MilkyWay";
        station2.parkedScooters = 1;

        return new ArrayList<>(asList(station1, station2));
    }
}
