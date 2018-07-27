package com.tieto.scooter.services;

import com.tieto.scooter.models.UserDto;
import com.tieto.scooter.utils.Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    MessagingService messagingService;

    private Random random = new Random();

    public boolean sendToken(String phoneNumber) {

        Integer token = random.nextInt(10000) + 1000;
        String message = "Your secret token is: " + token.toString();

        boolean result = messagingService.sendMessage(phoneNumber, message);

        if (!result) {
            return false;
        }

        UserDto user = Dto.setup(new UserDto(), u -> {
            u.phoneNumber = phoneNumber;
            u.token = token.toString();
        });

        return true;
    }
}
