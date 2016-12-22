package com.jixianxueyuan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Course;

public interface CourseDao extends PagingAndSortingRepository<Course,Long>
{
	List<Course> findByPid(Long pid);
	
	@Query("SELECT c FROM  Course c WHERE c.courseTaxonomy.id=? AND type=? order by sortWeight DESC")
	List<Course> findByCourseTaxonomyIdAndType(Long courseCatalogueId, String type);
}
