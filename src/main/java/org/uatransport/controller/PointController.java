package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.uatransport.entity.Point;
import org.uatransport.entity.Transit;
import org.uatransport.service.PointService;
import org.uatransport.service.TransitService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/stop")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private final TransitService transitService;

  @GetMapping("/{id}")
  public Point getById(@PathVariable Integer id) {
    return pointService.getById(id);
  }

  @GetMapping(params = "street")
  public List<Point> getByStreet(@RequestParam("street") String street) {
    return pointService.getByStreet(street);
  }

  @GetMapping(params = "transit-id")
  public List<Point> getByTransitId(@RequestParam("transit-id") Integer id) {
    return pointService.getByTransitId(id);
  }

  @DeleteMapping("/{id}")
  public void deleteStop(@PathVariable Integer id) {
      pointService.delete(id);
  }

  @PostMapping("/{id}")
  public ResponseEntity<Point> add(
      @RequestBody(required = false) Point point, @PathVariable Integer id) {
      Point savedPoint = pointService.save(point);
    Transit transitToUpdate = transitService.getById(id);
    transitToUpdate.getPoints().add(point);
    transitService.update(transitToUpdate);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().query("id={id}")
          .buildAndExpand(savedPoint.getId()).toUri();

      return ResponseEntity.created(location).build();
  }

  @PutMapping("/{id}")
  public Point update(@RequestBody Point point, @PathVariable Integer id) {
    return pointService.update(point.setId(id));
  }
}
