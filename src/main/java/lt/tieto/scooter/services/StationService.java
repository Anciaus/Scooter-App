package lt.tieto.scooter.services;

import lt.tieto.scooter.models.StationDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static lt.tieto.scooter.utils.Dto.setup;
import static java.util.Arrays.asList;

@Service
public class StationService {

    public List<StationDto> getStations() {
        return new ArrayList<>(asList(
                setup(new StationDto(), station -> {
                    station.name = "Andromeda";
                    station.parkedScooters = 2;
                }),
                setup(new StationDto(), station -> {
                    station.name = "MilkyWay";
                    station.parkedScooters = 1;
                })
        ));
    }
}
