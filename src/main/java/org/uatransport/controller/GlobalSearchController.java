package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.uatransport.config.GlobalSearch;
import org.uatransport.config.GlobalSearchSpecification;
import org.uatransport.config.StopSearchSpecification;
import org.uatransport.entity.Transit;
import org.uatransport.entity.Stop;
import org.uatransport.service.TransitService;
import org.uatransport.service.StopService;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/search")
public class GlobalSearchController {

    private final TransitService transitService;

    private final StopService stopService;

    @GetMapping(params = "search")
    public ResponseEntity<List<Transit>> getAll(@RequestParam("search") String search) {
        GlobalSearchSpecification globalSearchSpecification = new GlobalSearchSpecification(new GlobalSearch(search));
        return new ResponseEntity<>(transitService.getAll(globalSearchSpecification), HttpStatus.OK);
    }
    @GetMapping(params = "searchStop")
    public ResponseEntity<List<Stop>> getAllStops(@RequestParam("searchStop") String search) {
        StopSearchSpecification stopSearchSpecification = new StopSearchSpecification(new GlobalSearch(search));
        return new ResponseEntity<>(stopService.getAll(stopSearchSpecification), HttpStatus.OK);
    }
}
