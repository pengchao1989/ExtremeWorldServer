package com.jixianxueyuan.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Danmu;

public interface DanmuDao extends PagingAndSortingRepository<Danmu, Long>{

	List<Danmu> findByVideoId(Long id);
}
