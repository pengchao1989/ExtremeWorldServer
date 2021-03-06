package com.jixianxueyuan.repository.biz;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.biz.Category;

public interface CategoryDao extends  PagingAndSortingRepository<Category,Long>{

	/*@Query("SELECT t FROM Category t LEFT JOIN t.hobby h WHERE h.id=?")*/
	List<Category> findByHobbyId(Long hobbyId);
}
