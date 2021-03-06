package com.jixianxueyuan.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Discussion;
import com.jixianxueyuan.repository.DiscussionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class DiscussionService {

	@Autowired
	DiscussionDao discussionDao;
	
	public Discussion getDiscussion(Long id)
	{
		return discussionDao.findOne(id);
	}
	
	public void saveDiscussion(Discussion entity)
	{
		entity.setExcerpt(entity.getTitle());
		discussionDao.save(entity);
	}
	
	public Page<Discussion> getAll(int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return discussionDao.findAll(pageRequest);
				
	}
	
	public Page<Discussion> getByHobby(Long hobbyId, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return discussionDao.findByHobbyId(hobbyId, pageRequest);
	}
	
	public Page<Discussion> getByTaxonomy(Long taxonomyId, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return discussionDao.findByTaxonomyId(taxonomyId, pageRequest);
	}
	
	public Page<Discussion> getByHobbyAndTaxonomy(Long hobbyId, Long taxonomyId, int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return discussionDao.findByHobbyIdAndTaxonomyId(hobbyId, taxonomyId, pageRequest);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
