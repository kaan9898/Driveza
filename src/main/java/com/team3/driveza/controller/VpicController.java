package com.team3.driveza.controller;


import com.team3.driveza.service.VpicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vpic")
public class VpicController {

    private final VpicService vpicService;

    @GetMapping("/makes")
    public List<String> makes() {
        return vpicService.getAllMakes();
    }

    @GetMapping("/models")
    public List<String> models(@RequestParam String make) {
        return vpicService.getModelsForMake(make);

    }
}
