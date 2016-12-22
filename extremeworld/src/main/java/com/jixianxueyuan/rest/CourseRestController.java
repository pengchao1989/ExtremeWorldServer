package com.jixianxueyuan.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.entity.Course;
import com.jixianxueyuan.entity.Topic;
import com.jixianxueyuan.rest.dto.CourseDTO;
import com.jixianxueyuan.rest.dto.MyPage;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.rest.dto.TopicDTO;
import com.jixianxueyuan.service.CourseService;
import com.jixianxueyuan.service.TopicService;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/course")
public class CourseRestController {
	
	private static final String PAGE_SIZE = "30";
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	TopicService topicService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse get(@PathVariable("id") Long id)
	{
		Course course = courseService.getCourse(id);
		
		CourseDTO courseDTO = BeanMapper.map(course, CourseDTO.class);
		
		return MyResponse.ok(courseDTO,true);
	}
	
	@RequestMapping(value = "/explain/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse explainList(@PathVariable("id") Long id,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<Topic> explains = topicService.getAllTopicByCourseAndMagicType(id, TopicType.magicExplain, pageNumber, pageSize, sortType);
		
		MyPage<TopicDTO,Topic>  topicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, explains);
		
		return MyResponse.ok(topicPage,true);
	}
	
	

}
