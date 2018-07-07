package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.uatransport.entity.Stop;
import org.uatransport.entity.Transit;
import org.uatransport.service.StopService;
import org.uatransport.service.TransitService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stop")
@RequiredArgsConstructor
public class StopController {
    private final StopService stopService;
    private final TransitService transitService;

    @GetMapping("/{id}")
    public Stop getById(@PathVariable Integer id) {
        return stopService.getById(id);
    }

    @GetMapping(params = "transit-id")
    public List<Stop> getByTransitId(@RequestParam("transit-id") Integer id) {
        return stopService.getByTransitId(id);
    }

    @GetMapping
    public List<Stop> getByTransitIdAndDirection(@RequestParam("id") Integer id,
            @RequestParam("dir") String direction) {
        return stopService.getByTransitIdAndDirection(id, direction);
    }

    @DeleteMapping("/{id}")
    public void deleteStop(@PathVariable Integer id) {
        stopService.delete(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Stop> add(@RequestBody(required = false) Stop stop, @PathVariable Integer id) {
        Stop savedPoint = stopService.save(stop);
        Transit transitToUpdate = transitService.getById(id);
        transitToUpdate.getStops().add(stop);
        transitService.update(transitToUpdate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().query("id={id}")
                .buildAndExpand(savedPoint.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public Stop update(@RequestBody Stop stop, @PathVariable Integer id) {
        return stopService.update(stop.setId(id));
    }
}
