package lt.tieto.scooter.controllers;

import lt.tieto.scooter.models.StationDto;
import lt.tieto.scooter.services.StationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StationController.class)
public class StationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StationService stationService;

    @Test
    public void stations_should_return_available_station_Json() throws Exception {
        // given
        List<StationDto> stations = getStations();

        given(stationService.getStations()).willReturn(stations);

        // when/then
        mvc.perform(get("/api/station/stations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(stations.get(0).name)));
    }

    private List<StationDto> getStations() {
        StationDto station1 = new StationDto();
        station1.name = "testName";
        station1.parkedScooters = 2;

        StationDto station2 = new StationDto();
        station2.name = "testName2";
        station2.parkedScooters = 1;

        return new ArrayList<>(asList(station1, station2));
    }
}
