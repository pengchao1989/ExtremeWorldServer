package com.jixianxueyuan.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.rest.dto.*;
import com.jixianxueyuan.service.InviteService;
import com.jixianxueyuan.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/invite")
public class InviteRestController{
	
	@Autowired
	private InviteService inviteService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/inviter",method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse getInviter(
			@RequestParam(value = "loginName") String loginName){
		
		if(StringUtils.isNoneBlank(loginName)){
			User user = userService.findByLoginName(loginName);
			
			if(user != null){
				
				User inviter = user.getInviter();
				if(inviter != null){
					UserMinDTO userDTO = BeanMapper.map(inviter, UserMinDTO.class);
					return MyResponse.ok(userDTO);
				}
			}
		}
		
		return MyResponse.ok(null);
	}
}
