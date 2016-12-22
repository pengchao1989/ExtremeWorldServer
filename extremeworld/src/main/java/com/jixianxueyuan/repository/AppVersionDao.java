package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.AppVersion;

public interface AppVersionDao extends PagingAndSortingRepository<AppVersion, Long> {
	
}
