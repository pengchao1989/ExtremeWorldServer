package com.jixianxueyuan.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.rest.dto.UserMinDTO;
import com.jixianxueyuan.service.UserService;

@RestController
@RequestMapping(value = "/api/secure/v1/following")
public class FollowingRestCOntroller {
	
	private static Logger logger = LoggerFactory.getLogger(FollowingRestCOntroller.class);
	
	@Autowired
	UserService userInfoService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	List<UserMinDTO> list(@PathVariable("id") Long id)
	{
		List<User> userInfoList = userInfoService.getFollowings(id);
		
		List<UserMinDTO> userInfoDTOList = BeanMapper.mapList(userInfoList, UserMinDTO.class);
		return userInfoDTOList;
	}
	

}
