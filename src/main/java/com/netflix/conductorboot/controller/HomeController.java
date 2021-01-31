package com.netflix.conductorboot.controller;

import com.netflix.conductorboot.constants.Constants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile(Constants.EMBEDDED_OAUTH2)
public class HomeController {

    @RequestMapping("/openapi-ui")
    public String swaggerUI() {
        return "redirect:/swagger-ui.html";
    }


}
