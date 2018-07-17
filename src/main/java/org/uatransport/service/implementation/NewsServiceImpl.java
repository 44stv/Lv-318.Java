package org.uatransport.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uatransport.entity.News;
import org.uatransport.exception.ResourceNotFoundException;
import org.uatransport.repository.NewsRepository;
import org.uatransport.service.NewsService;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    public final NewsRepository newsRepository;

    @Override
    @Transactional(readOnly = true)
    public News getById(Integer id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("News with id '%s' not found", id)));
    }

    @Override
    @Transactional
    public News add(News news) {
        if (news == null) {
            throw new IllegalArgumentException("News object should not be null");
        }
        news.setCreatedDate(LocalDateTime.now());

        return newsRepository.save(news);
    }

    @Override
    @Transactional
    public News update(News news) {
        if (news == null) {
            throw new IllegalArgumentException("News object should not be null");
        }
        News updatedNews;
        if (newsRepository.existsById(news.getId())) {
            updatedNews = newsRepository.getOne(news.getId());
        } else {
            throw new ResourceNotFoundException(String.format("News with id '%s' not found", news.getId()));
        }
        if (news.getTitle() == null && news.getTitle().isEmpty()) {
            throw new ResourceNotFoundException("News title is null or empty");
        }
        updatedNews.setTitle(news.getTitle());
        if (news.getContent() == null && news.getContent().isEmpty()) {
            throw new ResourceNotFoundException("News content is null or empty");
        }
        updatedNews.setContent(news.getContent());
        return updatedNews;
    }

    @Override
    @Transactional
    public void delete(Integer newsId) {
        try {
            newsRepository.deleteById(newsId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format("News with id '%s' not found", newsId));
        }
    }

    @Override
    public Page<News> getAllOrderedByDate(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedDateDesc(pageable);
    }
}
