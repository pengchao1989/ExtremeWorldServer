package com.jixianxueyuan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Reply;

public interface ReplyDao extends PagingAndSortingRepository<Reply, Long>
{
/*	Page<Reply> findByTopicId(Long id, Pageable pageRequest);
	Page<Reply> findByUserId(Long id,  Pageable pageRequest);*/
	
	Page<Reply> findByTopicId(Long id, Pageable pageRequest);
}
