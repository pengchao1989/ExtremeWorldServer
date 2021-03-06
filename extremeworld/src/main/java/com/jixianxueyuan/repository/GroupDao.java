package com.jixianxueyuan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Group;

public interface GroupDao extends PagingAndSortingRepository<Group,Long>{
	Page<Group> findByHobbyIdAndStatus(Long hobbyId, int status, Pageable pageable);
}
