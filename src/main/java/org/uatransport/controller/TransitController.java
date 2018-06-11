package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.TransitDTO;
import org.uatransport.service.TransitService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transit")
@CrossOrigin
@RequiredArgsConstructor
public class TransitController {

    private final TransitService transitService;
    private final ModelMapper modelMapper;

    @GetMapping(params = "id")
    public ResponseEntity<TransitDTO> getTransitById(@RequestParam("id") Integer id) {
        TransitDTO transitDTO = modelMapper.map(transitService.getById(id), TransitDTO.class);
        return new ResponseEntity<>(transitDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransitDTO>> getAllTransits() {
        List<TransitDTO> transits = transitService.getAll()
            .stream()
            .map(transit -> modelMapper.map(transit, TransitDTO.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(transits, HttpStatus.OK);
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<List<TransitDTO>> getTransitsByCategoryId(@RequestParam("categoryId") Integer categoryId) {
        List<TransitDTO> transits = transitService.getAllByCategoryId(categoryId)
            .stream()
            .map(transit -> modelMapper.map(transit, TransitDTO.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(transits, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transit> addTransit(@RequestBody TransitDTO transitDTO) {
        Transit transit = modelMapper.map(transitDTO, Transit.class);
        return new ResponseEntity<>(transitService.add(transit), HttpStatus.CREATED);
    }

    @DeleteMapping(params = "id")
    public void deleteTransit(@RequestParam("id") Integer id) {
        transitService.delete(id);
    }

    @PutMapping(params = "id")
    public ResponseEntity<Transit> updateTransit(@RequestBody TransitDTO transitDTO,
                                                 @RequestParam("id") Integer id) {
        Transit updatedTransit = transitService.update(modelMapper.map(transitDTO, Transit.class).setId(id));
        return new ResponseEntity<>(updatedTransit, HttpStatus.OK);
    }
}