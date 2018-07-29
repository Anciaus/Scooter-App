package com.tieto.scooter.services;

import com.tieto.scooter.dataAccess.UserRepository;
import com.tieto.scooter.models.UserDto;
import com.tieto.scooter.utils.Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    private final MessagingService messagingService;

    private final UserRepository userRepository;

    private  Random random = new Random();

    @Autowired
    public UserService(MessagingService messagingService, UserRepository userRepository) {
        this.messagingService = messagingService;
        this.userRepository = userRepository;
    }

    public boolean sendToken(String phoneNumber) {

        String token = getToken();

        boolean result = messagingService.sendMessage(phoneNumber, token);

        if (!result) {
            return false;
        }

        UserDto user = Dto.setup(new UserDto(), u -> {
            u.phoneNumber = phoneNumber;
            u.token = token;
        });

        userRepository.createUser(user);

        return true;
    }

    private String getToken() {
        final int MIN = 1000;
        final int MAX = 9999;
        return String.valueOf(random.nextInt((MAX - MIN) + 1) + MIN);
    }
}
