package com.github.maheshyaddanapudi.netflix.conductorboot.controller;

import java.security.Principal;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;

@Profile({Constants.EXTERNAL_OAUTH2, Constants.EXTERNAL_ADFS, Constants.EMBEDDED_OAUTH2})
@RestController
public class ProfileController {

    @GetMapping("/userinfo")
    public Principal getUser(Principal principal)
    {
        return principal;
    }
}
