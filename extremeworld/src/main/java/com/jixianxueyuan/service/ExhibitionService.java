package com.jixianxueyuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Exhibition;
import com.jixianxueyuan.repository.ExhibitionDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ExhibitionService {

	@Autowired
	private ExhibitionDao exhibitionDao;
	
	public Page<Exhibition> getAllByHobbyAndStatus(Long hobbyId, int status, int pageNumber, int pageSize,String sortType){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		return exhibitionDao.findByHobbyIdAndStatus(hobbyId, status, pageRequest);
	}
	
	public Exhibition saveExhibition(Exhibition exhibition){
		return exhibitionDao.save(exhibition);
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
