package com.team3.driveza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    @GetMapping("/cars")
    public String cars() { return "cars"; }

    @GetMapping("/account")
    public String account() { return "account"; }

}
