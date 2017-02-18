package com.jixianxueyuan.rest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.validation.Validator;

import com.jixianxueyuan.config.*;
import com.jixianxueyuan.entity.*;
import com.jixianxueyuan.rest.dto.request.WeiXinWebPage;
import com.jixianxueyuan.service.*;
import com.jixianxueyuan.service.account.SecurityUser;
import org.apache.commons.lang3.StringUtils;
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

import com.jixianxueyuan.rest.dto.MyPage;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.rest.dto.TopicDTO;
import com.jixianxueyuan.rest.dto.TopicExtraDTO;
import com.jixianxueyuan.service.account.ShiroDbRealm.ShiroUser;
import sun.net.www.http.HttpClient;


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

	@Autowired
	PointService pointService;


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
		Long userId = getCurrentUserId();

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

		//每日发主题积分
		pointService.addPoint(PointType.TOPIC, userId);


		Topic result = topicService.getTopic(topic.getId());
		TopicDTO dto = BeanMapper.map(result, TopicDTO.class);

		return MyResponse.ok(dto, true);
	}

	@RequestMapping(value = "/submit_weixin_page",method = RequestMethod.POST,consumes = MediaTypes.JSON_UTF_8)
	public MyResponse submitWeiXinPage(
			@PathVariable String hobby,
            @RequestBody WeiXinWebPage weiXinWebPage)
	{

		String title_keyword = "    var msg_title = \"";
		String desc_keyword = "    var msg_desc = \"";
		String thumb_keyword = "    var msg_cdn_url = \"";

        Topic havingTopic = topicService.findByUrl(weiXinWebPage.getUrl());
        if (havingTopic != null){
            return MyResponse.err(MyErrorCode.WEI_XIN_PAGE_ALREADY_INCLUDED);
        }


		try {
			URL url = new URL(weiXinWebPage.getUrl());
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);


			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));

			HashMap<String,String> careParams = new HashMap<String,String>();
			String line = bufferedReader.readLine();
			while (line != null){
				if (line.contains(title_keyword)){
					String msg_title_value = line.replace(title_keyword, "");
                    msg_title_value = msg_title_value.replace("\";", "");
                    logger.debug(msg_title_value);
					careParams.put(title_keyword, msg_title_value);
				}
				if (line.contains(desc_keyword)){
					String msg_desc_value = line.replace(desc_keyword, "");
                    msg_desc_value = msg_desc_value.replace("\";", "");
					careParams.put(desc_keyword, msg_desc_value);
                    logger.debug(msg_desc_value);
				}
				if (line.contains(thumb_keyword)){
					String msg_thumb_value = line.replace(thumb_keyword, "");
                    msg_thumb_value = msg_thumb_value.replace("\";", "");
					careParams.put(thumb_keyword, msg_thumb_value);
                    logger.debug(msg_thumb_value);
				}

				line = bufferedReader.readLine();
			}

            bufferedReader.close();

            if (careParams.containsKey(title_keyword)){
                Topic topic = new Topic();
                topic.setStatus(TopicStatus.PUBLIC);
                topic.setType(TopicType.NEWS);

                Long  userId = getCurrentUserId();
                User user = new User();
                user.setId(userId);
                topic.setUser(user);

                if (topic.getHobbys() == null){
                    List<Hobby>  hobbyList = new ArrayList<Hobby>();
                    Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
                    Hobby hobbyEntity = new Hobby();
                    hobbyEntity.setId(hobbyId);
                    hobbyList.add(hobbyEntity);
                    topic.setHobbys(hobbyList);
                }

                topic.setTitle(careParams.get(title_keyword));
                topic.setUrl(weiXinWebPage.getUrl());
                topic.setExcerpt(careParams.get(desc_keyword));

                String thumbImgUrl = careParams.get(thumb_keyword);
                if (StringUtils.isNoneEmpty(thumbImgUrl)){

                    List<Media> mediaList = new ArrayList<Media>();
                    Media media = new Media();
                    media.setType("img");
                    media.setPath(thumbImgUrl);

                    mediaList.add(media);

                    MediaWrap mediaWrap = new MediaWrap();
                    mediaWrap.setMedias(mediaList);

                    topic.setMediaWrap(mediaWrap);
                }


                topicService.saveTopic(topic);

                TopicDTO topicDTO = BeanMapper.map(topic, TopicDTO.class);

                return MyResponse.ok(topicDTO, true);
            }

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


        return MyResponse.err(MyErrorCode.WEI_XIN_PAGE_ERROR);
	}
	

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        return securityUser.getId();
	}
}
