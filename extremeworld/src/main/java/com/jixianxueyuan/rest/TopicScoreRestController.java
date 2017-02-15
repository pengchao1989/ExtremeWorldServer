package com.jixianxueyuan.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.validation.Validator;

import com.jixianxueyuan.service.account.SecurityUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.config.HobbyPathConfig;
import com.jixianxueyuan.entity.Course;
import com.jixianxueyuan.entity.Hobby;
import com.jixianxueyuan.entity.Topic;
import com.jixianxueyuan.entity.TopicScore;
import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.entity.UserScore;
import com.jixianxueyuan.rest.dto.MyPage;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.rest.dto.TopicScoreDTO;
import com.jixianxueyuan.rest.dto.request.TopicScoreRequestDTO;
import com.jixianxueyuan.service.TopicScoreService;
import com.jixianxueyuan.service.TopicService;
import com.jixianxueyuan.service.UserScoreService;
import com.jixianxueyuan.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/topic_score")
public class TopicScoreRestController {

	private static final String PAGE_SIZE = "10";
	
	private static Logger logger = LoggerFactory.getLogger(TopicScoreRestController.class);
	
	@Autowired
	private TopicScoreService topicScoreService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private UserScoreService userScoreService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "topicId", defaultValue = "1") Long topicId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Page<TopicScore> pageTopicScore = topicScoreService.findAllByTopicId(topicId,pageNumber, pageSize, sortType);
		MyPage<TopicScoreDTO, TopicScore> myPageTopicScore = new MyPage<TopicScoreDTO, TopicScore>(TopicScoreDTO.class, pageTopicScore);
		return MyResponse.ok(myPageTopicScore, false);
	}
	
	@RequestMapping(method = RequestMethod.POST,consumes = MediaTypes.JSON_UTF_8)
	public MyResponse create(@RequestBody TopicScoreRequestDTO topicScoreRequestDTO){
		
		Long userId = getCurrentUserId();
		User user = new User();
		user.setId(userId);
		
		//判定唯一性
		TopicScore topicScore = topicScoreService.findByTopicIdAndUserId(topicScoreRequestDTO.getTopicId(), userId);
		if(topicScore == null){
			topicScore = new TopicScore();
		}
		
		topicScore.setScore(topicScoreRequestDTO.getScore() <= 10.0  ? topicScoreRequestDTO.getScore() : 10.0);
		topicScore.setUser(user);
		
		Topic topic = topicService.getTopic(topicScoreRequestDTO.getTopicId());
		topic.setId(topicScoreRequestDTO.getTopicId());
		topicScore.setTopic(topic);
		
		
		topicScoreService.save(topicScore);
		
		//求平均分、统计次数
		double totalAvgScore = topicScoreService.getTopicAvgScore(topicScoreRequestDTO.getTopicId());
		int topicTotalScoreCount = topicScoreService.getTopicScoreCount(topicScoreRequestDTO.getTopicId());
		topic.setScore(totalAvgScore);
		topic.setScoreCount(topicTotalScoreCount);
		topicService.saveTopic(topic);
		
		
		//更新个人总分
		if( topic != null && topic.getHobbys().size() == 1){
			
			Long topicUserId = topic.getUser().getId();
			Hobby hobby = topic.getHobbys().get(0);
			
			List<Topic> allChallengeTopicList = topicService.getAllUserChallenge(topicUserId, hobby.getId());
			if(allChallengeTopicList != null){
				
				//记录分数最大值
				HashMap<Long, Double> courseScoreHashMap = new HashMap<Long, Double>();
				for(Topic t : allChallengeTopicList){
					Course course = t.getCourse();
					if(course != null){
						double tempScore = 0.0;
						if(courseScoreHashMap.containsKey(course.getId())){
							tempScore = courseScoreHashMap.get(course.getId());
							if(tempScore < t.getScore()){
								courseScoreHashMap.put(course.getId(), t.getScore());
							}
						}else{
							courseScoreHashMap.put(course.getId(), t.getScore());
						}
					}
					
				}
				
				//累加
				Double totalScore = 0.0;
				for (Entry<Long, Double> entry : courseScoreHashMap.entrySet()) {
					totalScore += entry.getValue();
				}
				logger.debug("TotalScore=" + totalScore);
				System.out.println("TotalScore=" + totalScore);
				
				//更新总分
				UserScore userScore = userScoreService.findByUserIdAndHobbyId(topicUserId, hobby.getId());
				if(userScore == null){
					userScore = new UserScore();
					userScore.setHobby(hobby);
					userScore.setUser(topic.getUser());
				}
				userScore.setScore(totalScore);
				userScoreService.save(userScore);
			}
		}
		
		TopicScoreDTO topicScoreDTO = BeanMapper.map(topicScore, TopicScoreDTO.class);
		topicScoreDTO.setTopicAvgScore(totalAvgScore);
		topicScoreDTO.setTopicTotalScoreCount(topicTotalScoreCount);
		return MyResponse.ok(topicScoreDTO, false);
		
	}
	
	private Long getCurrentUserId() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        return securityUser.getId();
	}
}
