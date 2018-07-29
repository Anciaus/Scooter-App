package com.tieto.scooter.services;

import com.tieto.scooter.models.StationDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class StationServiceTest {

    private StationService stationService;

    @Before
    public void setUp() {
        stationService = new StationService();
    }

    @Test
    public void getStations_should_return_two_stations() {
        List<StationDto> actual = stationService.getStations();

        Assert.assertEquals(2, actual.size());
    }
}
