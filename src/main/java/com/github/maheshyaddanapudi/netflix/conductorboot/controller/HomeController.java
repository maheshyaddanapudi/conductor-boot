package com.github.maheshyaddanapudi.netflix.conductorboot.controller;

import com.github.maheshyaddanapudi.netflix.conductorboot.constants.Constants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@Profile(Constants.EMBEDDED_OAUTH2)
public class HomeController {

    @RequestMapping("/openapi-ui")
    public String swaggerUI() {
        return "redirect:/swagger-ui.html";
    }


}
