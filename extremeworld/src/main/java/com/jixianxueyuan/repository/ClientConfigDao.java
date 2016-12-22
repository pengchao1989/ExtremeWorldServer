package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.AppKey;

public interface ClientConfigDao extends PagingAndSortingRepository<AppKey, Long>{

}
