package com.jixianxueyuan.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Hobby;
import com.jixianxueyuan.entity.Taxonomy;
import com.jixianxueyuan.repository.HobbyDao;
import com.jixianxueyuan.repository.TaxonomyDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class BaseInfoService {

	@Autowired
	private TaxonomyDao taxonomyDao;
	
	@Autowired
	private HobbyDao hobbyDao;
	
	public List<Hobby> getBaseInfo()
	{
		//BaseInfo baseInfo = new BaseInfo();
		
		
		return (List<Hobby>) hobbyDao.findAll();
	}
	
	
	public class BaseInfo{
		
		
	}
	
	
	
}
