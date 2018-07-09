package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.Geotag;
import org.uatransport.service.GeotagService;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class GeotagController {
    private final GeotagService geotagService;

    @GetMapping("/all")
    public List<Geotag> getAllSupported() {
        return geotagService.getAllGeotags();
    }

    @GetMapping
    public Geotag getByCoordinates(@RequestParam Double latitude, @RequestParam Double longtitude) {
        return geotagService.whereIAm(latitude, longtitude);
    }
}
