package lt.tieto.scooter.controllers;

import lt.tieto.scooter.controllers.models.RegistrationRequest;
import lt.tieto.scooter.controllers.models.RegistrationResponse;
import lt.tieto.scooter.controllers.models.TokenRequest;
import lt.tieto.scooter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody TokenRequest tokenRequest) {

        boolean success = userService.sendToken(tokenRequest.phoneNumber);

        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/validate")
    public ResponseEntity<RegistrationResponse> validate(@RequestBody RegistrationRequest registrationRequest) {
        String sessionToken = userService.validateUser(registrationRequest);

        if (sessionToken != null) {
            RegistrationResponse registrationResponse = new RegistrationResponse();
            registrationResponse.securityToken = sessionToken;

            return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
