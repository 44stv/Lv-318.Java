package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.uatransport.config.SearchCategoryParam;
import org.uatransport.entity.ExtendableCategory;
import org.uatransport.entity.dto.CategoryDTO;
import org.uatransport.service.CategoryService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/top")
    public List<ExtendableCategory> getTop() {
        return categoryService.getListTopExtendableCategories();
    }

    @GetMapping
    public List<ExtendableCategory> search(SearchCategoryParam searchCategoryParam) {
        return categoryService.getAll(searchCategoryParam);
    }

    @GetMapping("/count")
    public List<CategoryDTO> getWithCountOfTransits(SearchCategoryParam searchCategoryParam) {
        return categoryService.getAllWithCountOfTransits(searchCategoryParam);
    }

    @GetMapping(value = "/img", produces = MediaType.IMAGE_PNG_VALUE)
    public void getImage(HttpServletResponse response, @RequestParam String link) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(link);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @PostMapping
    public ResponseEntity<ExtendableCategory> save(@RequestBody ExtendableCategory category) {
        if (category.getNextLevelCategory() != null) {
            category.setNextLevelCategory(categoryService.getById(category.getNextLevelCategory().getId()));
        }
        ExtendableCategory savedCategory = categoryService.save(category);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedCategory.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }

    @PutMapping("/{id}")
    public ExtendableCategory update(@RequestBody ExtendableCategory category, @PathVariable Integer id) {
        return categoryService.update(category.setId(id));
    }
}
