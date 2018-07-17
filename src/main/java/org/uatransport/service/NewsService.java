package org.uatransport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.uatransport.entity.News;

public interface NewsService {

    News getById(Integer id);

    News add(News news);

    News update(News news);

    void delete(Integer newsId);

    Page<News> getAllOrderedByDate(Pageable pageable);

}
