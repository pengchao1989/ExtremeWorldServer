package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Mood;

public interface MoodDao extends PagingAndSortingRepository<Mood,Long>{

}
