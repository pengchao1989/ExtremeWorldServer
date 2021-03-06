package com.jixianxueyuan.repository;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Topic;

public interface TopicDao extends PagingAndSortingRepository<Topic, Long>
{
/*	@Query("SELECT tb_topic FROM tb_course_topic  LEFT JOIN tb_topic ON tb_course_topic.topic_id = tb_topic.id WHERE tb_course_topic.course_id = ?")
	public Page<Topic> findByCourseId(Long courseId,Pageable pageable);*/
	public Page<Topic> findByCourseIdAndMagicTypeAndStatus(Long courseId, String magicType, int status, Pageable pageable);
	public Page<Topic> findByUserIdInAndStatus(Collection<Long> ids, int status, Pageable pageable);
	
	public Page<Topic> findByUserIdAndStatus(Long user, int status, Pageable pageable);
	public Page<Topic> findByUserIdAndStatusAndMediaWrapNotNull(Long user, int status, Pageable pageable);
	public Topic findByUrl(String url);
	
	public Page<Topic> findByTypeAndStatus(String type, int status, Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE t.user.id=? AND h.id=? AND type=? AND status=?")
	public List<Topic> findByUserIdAndHobbyIdAndTypeAndStatus(Long userId, Long hobbyId, String type, int status);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND t.status=?")
	public Page<Topic> findByHobby(Long hobbyId, int status, Pageable pageable);

	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND t.status=? AND t.createTime <?")
	public Page<Topic> findByHobbyAndCreateTime(Long hobbyId, int status, Date createTime, Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND status=?")
	public Page<Topic> findByHobbyAndType(Long hobbyId, String type, int status, Pageable pageable);

	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND status=? AND t.createTime <?")
	public Page<Topic> findByHobbyAndTypeAndCreateTime(Long hobbyId, String type, int status, Date createTime, Pageable pageable);

	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND t.taxonomy.id=? AND status=?")
	public Page<Topic> findByHobbyAndTypeAndTaxonomy(Long hobbyId, String type, Long taxonomyId, int status, Pageable pageable);

	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND t.taxonomy.id=? AND status=? AND t.createTime <?")
	public Page<Topic> findByHobbyAndTypeAndTaxonomyAndCreateTime(Long hobbyId, String type, Long taxonomyId, int status, Date createTime, Pageable pageable);

	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND status=? AND t.fine=1")
	public Page<Topic> findByHobbyAndFine(Long hobbyId, int status, Pageable pageable);
	

	
	//求总分(总分=每个course最高的那个得分 累加)
/*	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND t.user.id=? AND type=? AND status=? ORDER BY t.score DESC")
	public Double getUserTypeSumScore(Long hobbyId,  Long userId, String type, int status);*/
	
}
