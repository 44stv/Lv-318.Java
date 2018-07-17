package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uatransport.entity.Feedback;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.TransitDTO;
import org.uatransport.service.FeedbackService;
import org.uatransport.service.TransitService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transit")
@RequiredArgsConstructor
public class TransitController {
    private final TransitService transitService;
    private final ModelMapper modelMapper;
    private final FeedbackService feedbackService;

    @GetMapping("/{id}")
    public TransitDTO getTransitById(@PathVariable Integer id) {
        return modelMapper.map(transitService.getById(id), TransitDTO.class);
    }

    @GetMapping
    public List<TransitDTO> getAllTransits() {
        return transitService.getAll().stream().map(transit -> modelMapper.map(transit, TransitDTO.class))
            .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "categoryTransits")
    @GetMapping(params = "categoryId")
    public Page<TransitDTO> getTransitsByCategoryId(@RequestParam("categoryId") Integer categoryId, Pageable pageable) {
        return transitService.getAllByCategoryIdByPage(categoryId, pageable)
            .map(transit -> modelMapper.map(transit, TransitDTO.class));
    }

    @GetMapping(params = "nextLevelCategoryId")
    public List<TransitDTO> getTransitsByNextLevelCategoryId(
        @RequestParam("nextLevelCategoryId") Integer nextLevelCategoryId) {
        return transitService.getAllByNextLevelCategoryId(nextLevelCategoryId).stream()
            .map(transit -> modelMapper.map(transit, TransitDTO.class)).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "cityTransits")
    @GetMapping(params = "nextLevelCategoryName")
    public Page<TransitDTO> getTransitsByNextLevelCategoryName(
        @RequestParam("nextLevelCategoryName") String nextLevelCategoryName, Pageable pageable) {
        return transitService.getAllByNextLevelCategoryNameByPage(nextLevelCategoryName, pageable)
            .map(transit -> modelMapper.map(transit, TransitDTO.class));
    }

    @PostMapping
    public ResponseEntity<Transit> addTransit(@RequestBody TransitDTO transitDTO) {
        Transit transit = modelMapper.map(transitDTO, Transit.class);
        return new ResponseEntity<>(transitService.add(transit), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteTransit(@PathVariable Integer id) {
        transitService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transit> updateTransit(@RequestBody TransitDTO transitDTO, @PathVariable Integer id) {
        Transit updatedTransit = transitService.update(modelMapper.map(transitDTO, Transit.class).setId(id));
        return new ResponseEntity<>(updatedTransit, HttpStatus.OK);
    }


    @GetMapping("/user/{id}")
    public List<TransitDTO> getAllTransitsByUserId(@PathVariable Integer id) {
        List<Transit> transits = feedbackService.getByUserId(id).stream().map(Feedback::getTransit).
            distinct().limit(5).collect(Collectors.toList());
        return transits.stream().map(transit -> modelMapper.map(transit, TransitDTO.class)).collect(Collectors.toList());

    }

    @GetMapping("/{name}/{id}")
    public TransitDTO getTransitByNameAndCategoryId(@PathVariable String name, @PathVariable Integer id) {
        Transit transit = transitService.findByNameAndCategoryId(name, id);
        return modelMapper.map(transit, TransitDTO.class);
    }


}
