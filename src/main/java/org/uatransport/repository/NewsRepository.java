package org.uatransport.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.uatransport.entity.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {

    Page<News> findAllByOrderByCreatedDateDesc(Pageable pageable);

}
