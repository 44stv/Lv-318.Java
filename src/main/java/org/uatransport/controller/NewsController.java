package org.uatransport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uatransport.entity.News;
import org.uatransport.service.NewsService;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/{id}")
    public News getById(@PathVariable Integer id) {
        return newsService.getById(id);
    }

//    @Cacheable(cacheNames = "news")
    @GetMapping
    public Page<News> getNews(Pageable pageable) {
        return newsService.getAllOrderedByDate(pageable);
    }


}
