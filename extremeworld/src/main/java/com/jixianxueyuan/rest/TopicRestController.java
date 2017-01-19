package com.jixianxueyuan.rest;


import java.util.List;

import javax.validation.Validator;

import com.jixianxueyuan.service.account.SecurityUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.config.HobbyPathConfig;
import com.jixianxueyuan.config.TopicStatus;
import com.jixianxueyuan.config.TopicType;
import com.jixianxueyuan.entity.Collection;
import com.jixianxueyuan.entity.Hobby;
import com.jixianxueyuan.entity.Topic;
import com.jixianxueyuan.entity.TopicScore;
import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.rest.dto.MyPage;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.rest.dto.TopicDTO;
import com.jixianxueyuan.rest.dto.TopicExtraDTO;
import com.jixianxueyuan.service.CollectionService;
import com.jixianxueyuan.service.TopicScoreService;
import com.jixianxueyuan.service.TopicService;
import com.jixianxueyuan.service.UserService;
import com.jixianxueyuan.service.account.ShiroDbRealm.ShiroUser;


@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/topic")
/*@PreAuthorize("authenticated and hasPermission('user')")*/
/*@PreAuthorize("hasAuthority('ROLE_USER')")*/
public class TopicRestController
{
	private static Logger logger = LoggerFactory.getLogger(TopicRestController.class);
			
	private static final String PAGE_SIZE = "20";
	
	@Autowired
	TopicService topicService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CollectionService collectionService;
	
	@Autowired
	private TopicScoreService topicScoreService;


	@RequestMapping( method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public  MyResponse list(
			@PathVariable String hobby,
			@RequestParam (value = "type", defaultValue = "all") String type,
			@RequestParam (value = "magicType", defaultValue = "") String magicType,
			@RequestParam (value = "courseId", defaultValue = "0") long courseId,
			@RequestParam(value = "taxonomyId", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		Page<Topic> topicPageSource = null;
		
		switch(type)
		{
		case TopicType.ALL:
			topicPageSource = topicService.getTopicByHobby(hobbyId, pageNumber, pageSize, sortType);
			break;
		case TopicType.DISCUSS:
		case TopicType.NEWS:
		case TopicType.VIDEO:
		case TopicType.S_VIDEO:
			if(0 == taxonomyId){
				topicPageSource = topicService.getTopicByHobbyAndType(hobbyId, type, pageNumber, pageSize, sortType);
			}else{
				topicPageSource = topicService.getTopicByHobbyAndTypeAndTaxonomy(hobbyId, type, taxonomyId, pageNumber, pageSize, sortType);
			}
			break;
		case TopicType.COURSE:
			topicPageSource = topicService.getAllTopicByCourseAndMagicType(courseId, magicType, pageNumber, pageSize, sortType);
			break;
		}
		
		MyPage<TopicDTO, Topic> topicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPageSource);
		
		return MyResponse.ok(topicPage,true);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse get(@PathVariable("id") Long topicId){
		
		Long userId = getCurrentUserId();
		Topic topic = topicService.getTopic(topicId);
		if(topic == null){
			String message = "话题不存在(id:" + topicId + ")";
			logger.warn(message);
			throw new RestException(HttpStatus.NOT_FOUND, message);
		}
		
		TopicDTO topicDto = BeanMapper.map(topic, TopicDTO.class);
		
		Collection collection = collectionService.findByUserIdAndTopicIdAndStatus(userId, topicId, TopicStatus.PUBLIC);
		if(collection != null){
			topicDto.setCollected(true);
		}
		
		User currentUser = userService.getUser(userId);
		
		List<User> agreeUserList = topic.getAgrees();
		if(agreeUserList != null && agreeUserList.contains(currentUser)){
			topicDto.setAgreed(true);
		}
/*		if(agreeUserList != null && agreeUserList.size() > 0){
			for(User user : agreeUserList){
				if(user.getId() == userId){
					topicDto.setAgreed(true);
					break;
				}
			}
		}*/

		return MyResponse.ok(topicDto,true);
	}
	@RequestMapping(value = "/extra/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getTopicOfMe(@PathVariable("id") Long topicId){
		
		Long userId = getCurrentUserId();
		Topic topic = topicService.getTopic(topicId);
		if(topic == null){
			String message = "话题不存在(id:" + topicId + ")";
			logger.warn(message);
			throw new RestException(HttpStatus.NOT_FOUND, message);
		}
		
		
		TopicExtraDTO topicExtraDTO = new TopicExtraDTO();
		topicExtraDTO.setId(topicId);
		
		Collection collection = collectionService.findByUserIdAndTopicIdAndStatus(userId, topicId, TopicStatus.PUBLIC);
		if(collection != null){
			topicExtraDTO.setCollected(true);
		}
		
		User currentUser = userService.getUser(userId);
		
		List<User> agreeUserList = topic.getAgrees();
		if(agreeUserList != null && agreeUserList.contains(currentUser)){
			topicExtraDTO.setAgreed(true);
		}
		
		//topic mark score 
		TopicScore topicScore = topicScoreService.findByTopicIdAndUserId(topicId, userId);
		if(topicScore != null){
			topicExtraDTO.setMyMarkScore(topicScore.getScore());
		}else{
			topicExtraDTO.setMyMarkScore(0);
		}
		
		return MyResponse.ok(topicExtraDTO,true);
	}
	
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getTopicByUser(@PathVariable("id") Long userId,
			@RequestParam (value = "type", defaultValue = "all") String type,
			@RequestParam(value = "taxonomyId", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		Page<Topic> topicPage = topicService.getTopicByUser(userId, pageNumber, pageSize, sortType);
		
		MyPage<TopicDTO, Topic> myTopicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPage);
		
	
		return MyResponse.ok(myTopicPage,true);
	}
	
	@RequestMapping(value = "/fine", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getTopicOfFine(
			@PathVariable String hobby,
			@RequestParam (value = "type", defaultValue = "all") String type,
			@RequestParam(value = "taxonomyId", defaultValue = "0") Long taxonomyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType)
	{
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		
		Page<Topic> topicPage = topicService.getFineTopic(hobbyId,pageNumber, pageSize, sortType);
		
		MyPage<TopicDTO, Topic> myTopicPage = new MyPage<TopicDTO, Topic>(TopicDTO.class, topicPage);
		
		return MyResponse.ok(myTopicPage,true);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody Topic topic, UriComponentsBuilder uriBuilder)
	{
		//fix ios hobby
		List<Hobby> hobbyList = topic.getHobbys();
		if(hobbyList != null && hobbyList.size() == 1){
			if(hobbyList.get(0).getId() == 3L){
				Hobby skateboardHobby = new Hobby();
				skateboardHobby.setId(1L);
				hobbyList.add(skateboardHobby);
				topic.setHobbys(hobbyList);
			}
		}
		
		topic.setStatus(TopicStatus.PUBLIC);
		topicService.saveTopic(topic);
		

		Topic result = topicService.getTopic(topic.getId());
		TopicDTO dto = BeanMapper.map(result, TopicDTO.class);

		return MyResponse.ok(dto, true);
	}
	

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        return securityUser.getId();
	}
}
