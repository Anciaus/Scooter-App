package lt.tieto.scooter.services;

import lt.tieto.scooter.controllers.models.RegistrationRequest;
import lt.tieto.scooter.dtos.UserDto;
import lt.tieto.scooter.repos.UserRepository;
import lt.tieto.scooter.security.JwtTokenProvider;
import lt.tieto.scooter.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

@Service
public class UserService {

    private final boolean useMessagingService;
    private final MessagingService messagingService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private  Random random = new Random();

    @Autowired
    public UserService(
            @Value("${useMessagingService}") boolean useMessagingService,
            MessagingService messagingService,
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider) {
        this.useMessagingService = useMessagingService;
        this.messagingService = messagingService;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean sendToken(String phoneNumber) {

        String token = getToken();

        if (useMessagingService) {
            boolean result = messagingService.sendMessage(phoneNumber, token);

            if (!result) {
                return false;
            }
        }

        UserDto user = new UserDto();
        user.phoneNumber = phoneNumber;
        user.token = token;

        userRepository.createUser(user);

        return true;
    }

    public String validateUser(RegistrationRequest registrationRequest) {
        UserDto user = userRepository.getUserByPhoneNumber(registrationRequest.phoneNumber);

        if (user == null || !user.token.equalsIgnoreCase(registrationRequest.token)) {
            return null;
        }

        List<Role> roles = new ArrayList<>(asList(Role.ROLE_CLIENT));
        return jwtTokenProvider.createToken(registrationRequest.phoneNumber, roles);
    }

    private String getToken() {
        final int MIN = 1000;
        final int MAX = 9999;
        return String.valueOf(random.nextInt((MAX - MIN) + 1) + MIN);
    }
}
