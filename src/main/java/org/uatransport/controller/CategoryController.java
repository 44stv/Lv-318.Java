package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.uatransport.config.SearchCategoryParam;
import org.uatransport.config.SearchSpecification;
import org.uatransport.entity.ExtendableCategory;
import org.uatransport.entity.NonExtendableCategory;
import org.uatransport.service.CategoryService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(value = "/top/")
    public ResponseEntity<List<ExtendableCategory>> getTopCategories() {
        return new ResponseEntity<>(categoryService.getListTopExtendableCategories(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ExtendableCategory>> search(SearchCategoryParam searchCategoryParam) {
        SearchSpecification specification = new SearchSpecification(searchCategoryParam, categoryService);

        return new ResponseEntity<>(categoryService.getAll(specification), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExtendableCategory> save(@RequestBody ExtendableCategory category) {
        ExtendableCategory savedCategory = categoryService.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ExtendableCategory> update(@RequestBody ExtendableCategory category, @PathVariable Integer id) {
        ExtendableCategory updatedCategory = categoryService.update(category.setId(id));
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @GetMapping(value = "/img", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImage(HttpServletResponse response, @RequestParam String link) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(link);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }
}
