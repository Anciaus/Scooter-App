package com.tieto.scooter.services;

import com.tieto.scooter.models.StationDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class StationServiceTest {

    @TestConfiguration
    static class StationServiceContextConfiguration {

        @Bean
        public StationService stationService() {
            return new StationService();
        }
    }

    @Autowired
    private StationService stationService;

    @Test
    public void getStations_should_return_two_stations() {
        List<StationDto> actual = stationService.getStations();

        Assert.assertEquals(2, actual.size());
    }
}
