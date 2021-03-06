package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.config.GlobalSearch;
import org.uatransport.config.GlobalSearchSpecification;
import org.uatransport.entity.Transit;
import org.uatransport.service.TransitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/search")
public class GlobalSearchController {

    private final TransitService transitService;

    @GetMapping
    public ResponseEntity<List<Transit>> getAll(@RequestParam("search") String search,
            @RequestParam("city") String city) {
        GlobalSearchSpecification globalSearchSpecification = new GlobalSearchSpecification(
                new GlobalSearch(search, city));
        return new ResponseEntity<>(transitService.getAll(globalSearchSpecification), HttpStatus.OK);
    }
}
