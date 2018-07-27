package com.tieto.scooter.controllers;

import com.tieto.scooter.controllers.models.TokenRequestModel;
import com.tieto.scooter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/token")
    public HttpStatus token(@RequestBody TokenRequestModel tokenRequest) {

        boolean success = userService.sendToken(tokenRequest.phoneNumber);

        if (success) {
            return HttpStatus.OK;
        }

        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/validate")
    public void validate() {

    }
}
