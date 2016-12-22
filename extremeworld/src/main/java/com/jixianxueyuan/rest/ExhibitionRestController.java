package com.jixianxueyuan.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.config.ExhibitionAction;
import com.jixianxueyuan.config.HobbyPathConfig;
import com.jixianxueyuan.config.TopicStatus;
import com.jixianxueyuan.entity.Exhibition;
import com.jixianxueyuan.entity.Hobby;
import com.jixianxueyuan.entity.Media;
import com.jixianxueyuan.entity.MediaWrap;
import com.jixianxueyuan.entity.Topic;
import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.entity.VideoDetail;
import com.jixianxueyuan.rest.dto.ExhibitionDTO;
import com.jixianxueyuan.rest.dto.MyPage;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.rest.dto.TopicDTO;
import com.jixianxueyuan.service.ExhibitionService;
import com.jixianxueyuan.service.TopicService;
import com.jixianxueyuan.service.account.ShiroDbRealm.ShiroUser;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/exhibition")
public class ExhibitionRestController {

	private static final String PAGE_SIZE = "5";
	
	
	@Autowired
	ExhibitionService exhibitionService;
	
	@Autowired
	TopicService topicService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		Long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		Page<Exhibition> exhibitionPage = exhibitionService.getAllByHobbyAndStatus(hobbyId, TopicStatus.PUBLIC, pageNumber, pageSize, sortType);
		
		MyPage<ExhibitionDTO, Exhibition> exhibitionMyPage = new MyPage<ExhibitionDTO,Exhibition >(ExhibitionDTO.class, exhibitionPage);
		
		return MyResponse.ok(exhibitionMyPage,true);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@PathVariable String hobby,
			@RequestBody Exhibition exhibition, UriComponentsBuilder uriBuilder){
		
		if(exhibition.getHobbys() == null){
			List<Hobby> hobbys = new ArrayList<Hobby>();
			Hobby currentHobby = new Hobby();
			Long currentHobbyId = HobbyPathConfig.getHobbyId(hobby);
			currentHobby.setId(currentHobbyId);
			
			hobbys.add(currentHobby);
			exhibition.setHobbys(hobbys);
		}
		
		String action = exhibition.getAction();
		if(ExhibitionAction.OPEN_TOPIC.equals(action)){
			Topic topic = topicService.getTopic(exhibition.getTargetId());
			if(topic != null){
				
				if(StringUtils.isBlank(exhibition.getTitle())){
					exhibition.setTitle(topic.getTitle());
				}
				
				TopicDTO topicDTO = BeanMapper.map(topic, TopicDTO.class);
				String topicDTOJson = JsonMapper.nonEmptyMapper().toJson(topicDTO);
				exhibition.setData(topicDTOJson);
				
				if(StringUtils.isBlank(exhibition.getImg())){
					
					VideoDetail videoDetail = topic.getVideoDetail();
					if(videoDetail != null){
						exhibition.setImg(videoDetail.getThumbnail());
					}else{
						MediaWrap mediawrap = topic.getMediaWrap();
						if(mediawrap != null){
							List<Media> medias = mediawrap.getMedias();
							if(medias != null && medias.size() > 0){
								exhibition.setImg(medias.get(0).getPath());
							}
						}
					}
				}
			}
		}
		
		Long userId = getCurrentUserId();
		User user = new User();
		user.setId(userId);
		exhibition.setUser(user);
		
		exhibition.setStatus(TopicStatus.PUBLIC);
		exhibitionService.saveExhibition(exhibition);
		
		
		ExhibitionDTO exhibitionDTO = BeanMapper.map(exhibition, ExhibitionDTO.class);
		
		return MyResponse.ok(exhibitionDTO, true);
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
}
