package com.jixianxueyuan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.UserScore;

public interface UserScoreDao extends PagingAndSortingRepository<UserScore, Long>{

	public UserScore findByUserIdAndHobbyId(Long userId, Long hobbyId);
	
	public Page<UserScore> findByHobbyId(Long hobbyId, Pageable pageable);
}
