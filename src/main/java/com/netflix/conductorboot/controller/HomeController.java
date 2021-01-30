package com.netflix.conductorboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/boot-swagger-ui")
    public String swaggerUI() {
        return "redirect:/swagger-ui.html";
    }

}
