package com.jixianxueyuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Collection;
import com.jixianxueyuan.repository.CollectionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class CollectionService {

	private CollectionDao collectionDao;
	
	public Page<Collection> getCollectionByUserAndStatus(long userId, int status, int pageNumber, int pageSize,String sortType){
		
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return collectionDao.findByUserIdAndStatus(userId, status, pageRequest);
	}
	
	public Collection getCollectionByUserAndTopic(long userId, long topicId){
		
		return collectionDao.findByUserIdAndTopicId(userId, topicId);
	}
	
	public Collection findByUserIdAndTopicIdAndStatus(Long userId,
			Long topicId, int status) {
		return collectionDao.findByUserIdAndTopicIdAndStatus(userId, topicId, status);
	}
	
	public Collection saveCollection(Collection collection){
		return collectionDao.save(collection);
	}

	public CollectionDao getCollectionDao() {
		return collectionDao;
	}
	@Autowired
	public void setCollectionDao(CollectionDao collectionDao) {
		this.collectionDao = collectionDao;
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
}
