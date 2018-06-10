package org.uaTransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uaTransport.entity.Stop;
import org.uaTransport.entity.Transit;
import org.uaTransport.service.StopService;
import org.uaTransport.service.TransitService;

import java.util.List;

@RestController
@RequestMapping("/stop")
@CrossOrigin ()
@RequiredArgsConstructor
public class StopController {

  private final StopService stopService;
  private final TransitService transitService;

  @GetMapping("/{id}")
  public ResponseEntity<Stop> getById(@PathVariable Integer id) {
    return new ResponseEntity<>(stopService.getById(id), HttpStatus.OK);
  }

  @GetMapping(params = "street")
  public ResponseEntity<List<Stop>> getByStreet(@RequestParam("street") String street) {
    return new ResponseEntity<>(stopService.getByStreet(street), HttpStatus.OK);
  }

  @GetMapping(params = "transit-id")
  public ResponseEntity<List<Stop>> getByTransitId(@RequestParam("transit-id") Integer id) {
    return new ResponseEntity<>(stopService.getByTransitId(id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public void deleteStop(@PathVariable Integer id) {
    stopService.delete(id);
  }


  @PostMapping("/{id}")
  public ResponseEntity<Stop> add(@RequestBody(required = false) Stop stop, @PathVariable Integer id) {
    Transit transitToUpdate = transitService.getById(id);
    transitToUpdate.getStops().add(stop);
    transitService.update(transitToUpdate);
    Stop savedStop = stopService.save(stop);
    return new ResponseEntity<>(stopService.save(stop), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Stop> update(@RequestBody Stop stop, @PathVariable Integer id) {
    Stop savedStop = stopService.update(stop.setId(id));
    return new ResponseEntity<>(savedStop, HttpStatus.CREATED);
  }

}
