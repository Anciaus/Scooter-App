package com.tieto.scooter.controllers;

import com.tieto.scooter.controllers.models.TokenRequestModel;
import com.tieto.scooter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody TokenRequestModel tokenRequest) {

        boolean success = userService.sendToken(tokenRequest.phoneNumber);

        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/validate")
    public void validate() {

    }
}
