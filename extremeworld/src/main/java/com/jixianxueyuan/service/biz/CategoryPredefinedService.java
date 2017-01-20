package com.jixianxueyuan.service.biz;

import java.util.List;

import com.jixianxueyuan.entity.biz.CategoryPredefined;
import com.jixianxueyuan.repository.biz.CategoryPredefinedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class CategoryPredefinedService {

	@Autowired
	CategoryPredefinedDao categoryPredefinedDao;
	
	public List<CategoryPredefined> getAll(){
		return (List<CategoryPredefined>) categoryPredefinedDao.findAll();
	}
}
