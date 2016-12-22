package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.News;

public interface NewsDao extends PagingAndSortingRepository<News, Long>{

}
