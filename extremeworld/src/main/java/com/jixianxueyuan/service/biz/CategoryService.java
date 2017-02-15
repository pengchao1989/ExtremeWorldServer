package com.jixianxueyuan.service.biz;

import java.util.List;

import com.jixianxueyuan.entity.biz.Category;
import com.jixianxueyuan.repository.biz.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class CategoryService {

	@Autowired
	CategoryDao categoryDao;

	public List<Category> getAllByHobbyId(long hobbyId){
		return categoryDao.findByHobbyId(hobbyId);
	}
}
